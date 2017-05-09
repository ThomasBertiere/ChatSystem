package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import gui.Vue;
import lib.Pair;
import model.Contact;
import model.Message;
import model.MessageType;
import model.Model;
import model.MsgCheck;
import model.MsgFile;
import model.MsgHello;
import model.MsgTxt;
import network.Network;

public class Controller implements ActionListener,Observer {
	

	/* ############################
	  
	  Classe controller du design 
	  pattern MVC. Contient les 
	  principales fonctions du systeme
	  
	   ############################*/

	private int connected;
	private Model model ; 
	private Network network ; 
	private Vue vue ;
	//utilise pour recuperer les messages dans la fonction connection
	private BlockingQueue<Pair<Message,InetAddress>> fifo ; 
	//utilise pour recuper les messages dans la fonction check
	private BlockingQueue<Pair<Message,InetAddress>> fifoCheck ; 
	private int timerEnd ; 
	private CheckThread checkthread ; 
	private final String broadcast = "255.255.255.255" ; 
	//Attribut permettant la reception/envoi de fichier  
	private File f ; 
	
	
	
	public Controller(Model model,Network network, Vue vue){
		this.model=model;
		this.network=network;
		this.vue=vue;
		//on ajoute le controller a la vue, pour que l'action
		//listener de vue soit le contoller
		this.vue.addControlller(this);
		this.setConnected(0);
		this.vue.getConnexionWindows().displayConnexionWindows();
		//lancement des serveurs d'envoi et de reception
		this.network.launchServer();
		this.network.launchClient();
		fifo = new ArrayBlockingQueue<Pair<Message,InetAddress>>(10) ; 
		fifoCheck = new ArrayBlockingQueue<Pair<Message,InetAddress>>(10) ; 
		checkthread=new CheckThread(this, fifoCheck) ; 
	}

	public Model getModel() {
		return model;
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public Vue getVue() {
		return this.vue;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
	}
	
	public String getBroadcast() {
		return broadcast;
	}

	public int getTimerState() {
		return timerEnd ; 
	}
	
	public void setTimerState(int timerState) {
		timerEnd=timerState ; 
	}
	
	public File getFile() {
		return f ; 
	}
	
	//Fonction qui vérifie qu'un username est bien forme
	//(pas vide, pas d'espace,...)
	//renvoie false si l'username est bien forme
	public boolean usernameMalformed(String username){
		boolean res = false ;
		if (username=="") {
			res = true ; 
		}
		else if (username.contains("\n")) {
			res = true ; 
		}
		else if (username.contains(" ")){
			res = true ;
		}
		return res ;
	}
	
	//Fonction de deconnexion lancee lorsque l'utilisateur presse le 
	//bouton de deconnexion 
	public void deconnexion(){
		this.model.removeListConnectedUsers();
		//envoi d'un message goodbye.
		Message messageGoodbye = this.model.createMessage(this.model.getMySlef().getUsername(),broadcast, MessageType.GOODBYE, true, true);
		try {
			this.network.send(messageGoodbye,InetAddress.getByName(broadcast));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.model.removeMySelf();
		this.setConnected(0);
		//arret des threads, du reseau et fermeture de la fenetre 
		this.network.stopNetwork();
		this.checkthread.stopCheckThread();
		this.vue.getChatWindows().closeChatWindows();
		this.deconnexionPopUp();
	}
	
	protected void deconnexionPopUp() {
		this.vue.getChatWindows().launchDeconnexionPopUp();
	}
	
	//fonction de connexion, fonction lancee lorsque l'utilisateur presse
	//le bouton connexion sur la fenetre de connexion
	public void connexion(String username){
		boolean malformed = true ; 
		Timer timer = new Timer();
	
		//test si l'username est malforme, si c'est le cas un popup apparait et 
		//il ne peut se connecter
		if (this.usernameMalformed(username)){
			this.popUpConnexionFailed();
		} 
		else {
			malformed=false ; 
		}
		//on lance la connexion si le nom de l'utilisateur est correct
		if (malformed==false){
			//on recupere l'addresse ip de notre machine
			InetAddress ipLocal = null ; 
			try {
				ipLocal = Network.getIp() ;
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} 
			//création d'une liste d'utilisateur connecté dans model et vue
			this.model.createListConnectedUser();
			this.vue.InitListConnectedUsers(this.model.convertListToString());
			//creation de notre contact 
			Contact mySelf = model.createContact(username, ipLocal) ; 
			this.model.setMySlef(mySelf);
			//creation d'un message hello et envoi en broadcast
			Message msgHello = this.model.createMessage(username,broadcast,MessageType.HELLO,false,true);
			try {
				this.network.send(msgHello,InetAddress.getByName(broadcast)) ;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} 
			//creation d'un timer de 300ms
			timer.schedule(new TimerConnexion(timer,this), 300);
			this.setTimerState(0);
			//on relie la fonction update qui traite les messages recus (l'observer) a
			//la fonction de recpetion de messages dans Network (l'obersable)
			this.linkObservable();
			//tant que le timer n'est pas ecoule 
			while (this.getTimerState() == 0 ) {
				Pair<Message,InetAddress> p =null; 
				try {
					synchronized(fifo) {						
						//tant qu'un signal n'est pas recu (c'est a dire 
						//qu'aucun message n'a ete recu), on attend
						fifo.wait();
						if(this.getTimerState()==0) {
							//on recupere le message
							p=fifo.take();
							//si le message est du type Hello
							if(p.getE().getType()==MessageType.HELLO) {
								//si le message est un Hello OK
								if ( (((MsgHello)p.getE()).getContent().getE()==true) && (((MsgHello)p.getE()).getContent().getT()==true) ) {

									//on ajoute l'utilisateur à la liste des utilsateurs connectés (dans vue et model)
									addRemoteUser(this.model.createContact(p.getE().getUserSrc(),p.getT()));
								}
								//si le message est un Hello
								else if((((MsgHello)p.getE()).getContent().getE()==false) && (((MsgHello)p.getE()).getContent().getT()==true) ){
								}	
								//si un message Hello not Ok est recu
								else {
									//on supprime notre liste
									removeAllConnectedUsers();									
									//on affiche un popUp d'echec
									this.popUpConnexionFailed();
									this.setTimerState(1);
									//on stop le timer
									timer.cancel();
									//l'username est incorrecte donc on doit recommencer le processus
									malformed=true ;
									break ; 
								}
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			//si l'username est correcte
			if (malformed==false){
				//on devient connecte
				this.setConnected(1);
				//on ferme la fenetre de connexion et on lance celle du chat
				this.vue.getConnexionWindows().closeConnexionWindows();
				this.vue.getChatWindows().displayChatWindows();	
				//on lance le thread de check 
				this.checkthread.start();
			}
		}
	}
	
	protected void popUpConnexionFailed() {
		this.vue.getConnexionWindows().launchConnexionFailedPopUp();
	}
	

	//classe du timer utilise dans la connexion
	class TimerConnexion extends TimerTask {	
		private Timer timer ; 
		private Controller controller ; 
		
		public TimerConnexion(Timer timer, Controller controler) {
			this.controller=controler ; 
			this.timer=timer ; 
		}
		
		public void run(){
			synchronized(fifo){
				//on envoie un signal pour eviter d'être bloque par les fifo
				//on met ensuite l'etat du timer a 1, le timer est donc ecoule
				fifo.notify();
				controller.setTimerState(1);
				timer.cancel();
			}
		}	
	}
	
	//fonction d'envoi de message textuel a un utilisateur distant.
	//Cette fonction est lancee lorsque l'utilisateur appuie sur le bouton 
	//envoi de message
	public void send(String msgTxt,String username) {
		Contact c;
		try {
			//on recupere le contact selectionne
			c = this.model.getConnectedUser(username);
			//on cree un message contenant le texte a etre envoye et on l'envoie
			Message m = this.model.createMessageTXT(this.model.getMySlef().getUsername(),username, msgTxt) ; 
			this.network.send(m, c.getIp());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//fonction d'envoi de message contenant un fichier a un utilisateur distant.
	//Cette fonction est lancee lorsque l'utilisateur appuie sur le bouton 
	//selection d'un fichier
	public void sendFile(File f,String username) {
		Contact c ; 
		try {
			//on recupere le contact selectionne
			c=this.model.getConnectedUser(username) ;
			//on cree un message contenant le fichier a etre envoye et on l'envoie
			Message m = this.model.createMessageFile(this.model.getMySlef().getUsername(), username, f) ; 
			this.network.send(m, c.getIp());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	//fonction definissant les actions a realiser lorsque l'utilsateur
	//realise une action sur une interface graphique
	@Override
	public void actionPerformed(ActionEvent e) {
		//s'il presse le bouton deconnexion de la fentre de chat on lance la fonction deconnexion
		if(e.getSource()==this.vue.getChatWindows().getbDisconnect()){
			this.deconnexion();
		}
		//s'il presse le bouton connexion de la fentre de connexion on lance la fonction connexion
		else if (e.getSource()==this.vue.getConnexionWindows().getbConnexion()){
			this.connexion(this.vue.getConnexionWindows().getUsername());
		}
		//s'il presse le bouton envoi de message de la fentre de connexion on recupere le message 
		//tape, l'utilisateur distant selectionne, on lance la fonction d'envoi de message et
		//on affiche le message envoyé sur la fenetre de chat
		else if (e.getSource()==this.vue.getChatWindows().getbSendMessage()){
			String message =this.vue.getChatWindows().gettSaisieMessage().getText();
			//on efface le texte tape
			this.vue.getChatWindows().gettSaisieMessage().setText("");
			String dest = this.vue.getChatWindows().getselectedUser();
			this.send(message,dest);
			this.vue.getChatWindows().writeSentMessage(message, dest);
		}
		//s'il presse le bouton envoi de fichier de la fentre de connexion on recupere l'utilisateur
		//distant selectionne, on ouvre une fentre de selection de fichier, on recupere le fichier 
		//selectionne et on l'envoi au destinataire.
		else if(e.getSource()==this.vue.getChatWindows().getbSelectFile()) {
			String dest = this.vue.getChatWindows().getselectedUser();
			File f = this.vue.getChatWindows().openFileChooser() ; 
			if (f!=null){
				this.sendFile(f,dest);
				//on affiche le nom du fichier envoyé sur la fenetre de chat		
				this.vue.getChatWindows().writeSentMessage("Le fichier : "+f.getName(),dest);
			}
		}
		//s'il presse le bouton de reception de fichier de la fentre de connexion on ouvre une fenetre
		//de selection de fichier pour creer un fichier qui sera une copie du fichier recu.
		else if(e.getSource()==this.vue.getChatWindows().getbOpenFile()) {
			File ff = this.vue.getChatWindows().openFileSave() ; 
			//on copie le contenu du fichier recu dans un nouveau fichier situe sur
			//notre machine
			try {
				FileInputStream is = new FileInputStream(f) ; 
				FileOutputStream os = new FileOutputStream(ff) ; 
				
				byte[] bufF = new byte[1024] ; 
				int length ; 
				while((length = is.read(bufF))>0) {
					os.write(bufF, 0, length);
				}
				
				is.close();
				os.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			} 
			this.vue.getChatWindows().setEnableOpenFileSave(false);
		}
	}

	//fonction update realisant des actions specifiques en fonction du message recu. 
	//Cette fonction est l'observer de la fonction de recpetion de message de network
	//(l'observable). Celle ci est donc lancee a chaque fois qu'un message est recu par
	//l'observabe. Le message recu est contenu dans l'objet arg.
	@Override
	public void update(Observable obj, Object arg){
		//on utilise ? pour ne pas avoir de problème de TypeSafety sur le cast
		Pair<?,?> paireRecu = (Pair<?,?>)arg ; 
		//on cree le message recu
		Message messageRecu =  (Message)paireRecu.getE();
		//on cree l'adresse ip source
		InetAddress ipRecu = (InetAddress)paireRecu.getT() ;
		//on cree le destinataire source
		Contact contactDest = this.getModel().createContact(messageRecu.getUserSrc(), ipRecu);		
		//si on est connecte
		if (this.getConnected()==1){
			//si l'utilisateur source est different de nous même
			if(!(messageRecu.getUserSrc().equals(this.model.getMySlef().getUsername()))) {
				//si le message recu est de type hello
				if(messageRecu.getType()==MessageType.HELLO){
					//On test si on reçoit un message hello et pas un hello_ok ou hello_not_ok
					if((((MsgHello)messageRecu).getContent().getE()==false) && (((MsgHello)messageRecu).getContent().getT()==true)) {
						//on verfie que le nom de l'utilisateur distant n'est pas deja pris par un autre
						if(this.model.checkUsernameNotExists(contactDest.getUsername())){
							//si l'username est libre 
							//on cree un Hello_OK et on l'envoie à l'utilisateur source
							Message messageHelloOk=this.getModel().createMessage(this.getModel().getMySlef().getUsername(),contactDest.getUsername(),MessageType.HELLO,true,true);
							this.getNetwork().send(messageHelloOk,contactDest.getIp());
							//on notifie qu'il y a une nouvelle connexion
							this.checkthread.setNewConnection(true);
							//on ajoute l'utilisateur a notre liste d'utilisateur connecte
							addRemoteUser(contactDest);
						}
						else {
							//si l'utilisateur est deja pris on cree un message Hello_Not_Ok et on
							//l'envoie a l'utilisateur source
							Message messageHelloNotOk=this.getModel().createMessage(this.getModel().getMySlef().getUsername(),contactDest.getUsername(),MessageType.HELLO,true,false);
							this.getNetwork().send(messageHelloNotOk,contactDest.getIp());
						}
					}
				}
				//si le message recu est un goodbye on supprime l'utilisateur source de nos listes
				//d'utilisateurs connecte
				else if (messageRecu.getType()==MessageType.GOODBYE){
					removeRemoteUser(contactDest);
				}
				//si le message recu est du type check 
				else if (messageRecu.getType()==MessageType.CHECK){
					//si c'est un check (ack=false)
					if (((MsgCheck)messageRecu).getContent()==false){
						//on cree un message Check_OK et on l'envoie à l'utilisateur distant
						Message messageCheckOk=this.getModel().createMessage(this.getModel().getMySlef().getUsername(), contactDest.getUsername(), MessageType.CHECK, true, true);
						this.getNetwork().send(messageCheckOk,contactDest.getIp());
					}
					//si c'est un check_ok
					else {
						//on envoie un signal a la fifo du thread check et on empile le message 
						//recu dans cette file
						synchronized(fifoCheck) {
							this.fifoCheck.add((Pair<Message,InetAddress>)paireRecu);
							fifoCheck.notify();
						}
					}
				}
				//si le message recu est un message textuel
				else if(messageRecu.getType()==MessageType.TXT){
					//on affiche son contenu sur la conversation associe a l'utilisateur source
					this.getVue().getChatWindows().writeReceivedMessage(((MsgTxt)messageRecu).getContent(),contactDest.getUsername());
				}
				//si le message recu est un fichier, l'attribut fichier de cette classe prend le fichier reçu
				else if(messageRecu.getType()==MessageType.FILE){
					f = ((MsgFile)messageRecu).getContent() ;
					this.vue.getChatWindows().setEnableOpenFileSave(true);
					//on affiche le nom du fichier sur la conversation associe a l'utilisateur source
					this.getVue().getChatWindows().writeReceivedMessage("Le fichier : "+f.getName()+"\nAppuyer sur Open File pour le télécharger", contactDest.getUsername());
				}
			}
			//si l'utilisateur source est le meme que nous
			else{
				//si le message recu est de type hello
				if(messageRecu.getType()==MessageType.HELLO){
					//On test si on reçoit un message hello et pas un hello_ok ou hello_not_ok
					if((((MsgHello)messageRecu).getContent().getE()==false) && (((MsgHello)messageRecu).getContent().getT()==true)) {
						//l'utilisateur est deja pris, on cree un message Hello_Not_Ok et on
						//l'envoie a l'utilisateur source
						Message messageHelloNotOk=this.getModel().createMessage(this.getModel().getMySlef().getUsername(),contactDest.getUsername(),MessageType.HELLO,true,false);
						this.getNetwork().send(messageHelloNotOk,contactDest.getIp());
					}
				}
			}
		}
		//si on n'est pas connecte (donc en cours de connexion)
		else {
			//si le message recu est de type Hello
			if(messageRecu.getType()==MessageType.HELLO){
					//on envoie un signal a la fifo de connexion check et on empile le message 
					//recu dans cette file
					synchronized(fifo) {
						fifo.add(new Pair<Message,InetAddress>(messageRecu,ipRecu)) ; 
						fifo.notify();
					}
			}
		}
	}
	

	//fonction qui relie la fonction update qui traite les messages recus
	//(l'observer) a la fonction de recpetion de messages dans Network (l'obersable)
	public void linkObservable(){
		this.getNetwork().addObserver(this);
		new Thread(this.getNetwork()).start();
	}

	//fonction qui ajoute un utilisateur à la liste des utilisateurs connectes de vue 
	//et de model
	private void addRemoteUser(Contact remoteContact){
		this.vue.getChatWindows().addUser(remoteContact.getUsername());
		this.model.addListConnectedUser(remoteContact);
	}
	
	//fonction qui supprime un utilisateur de la liste des utilisateurs connectes de vue 
	//et de model
	private void removeRemoteUser(Contact remoteContact){
		this.vue.getChatWindows().removeUser(remoteContact.getUsername());
		
		this.getModel().removeConnectedUser(remoteContact.getUsername());
	}
	
	//fonction qui vide la liste des utilisateurs connectes de vue 
	//et de model
	private void removeAllConnectedUsers(){
		this.model.removeListConnectedUsers();
		this.vue.getChatWindows().removeListConnectedUsers();	
	}
	
}
