package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import controller.Controller;

public class ChatWindows extends JFrame implements MouseListener{
	

	/* ############################
	  
	  Classe ChatWindows definissant 
	  la fenetre de chat
	  
	   ############################*/

	private JLabel lConnectedUsers ; 
	private JList<RemoteUsers> jlListeConnectedUsers; 
	//table de hachage permettant d'associer une zone de texte a un utilisateur
	private Hashtable<String,JTextArea> htTConversation;
	//table de hachage permettant d'associer une scroll bar a un utilisateur
	private Hashtable<String,JScrollPane> htScrollBarConversation;
	private JButton bDisconnect;
	private JButton bSendMessage;
	private JButton bSelectFile;
	private JButton bOpenFile; 
	private JTextArea tSaisieMessage;
	private JPanel textConversationPanel;
	//liste des utilisateurs connectes
	private DefaultListModel<RemoteUsers> listConnectedUsers; 
	private JPanel listPanel;
	//represente l'utilisateur actuellement selectionne
	private String selectedUser;
	private JFileChooser fileChooser ; 
	private JFileChooser fileSave ; 
	
	public ChatWindows(){
		this.init();
	}
	
	public  DefaultListModel<RemoteUsers> getlistConnectedUsers() {
		return listConnectedUsers;
	}
	
	public JButton getbSelectFile() {
		return bSelectFile; 
	}
	
	public JButton getbDisconnect() {
		return bDisconnect;
	}	
	
	public JButton getbSendMessage() {
		return bSendMessage;
	}
	
	public JButton getbOpenFile() {
		return bOpenFile ; 
	}
	
	public String getselectedUser(){
		return selectedUser;
	}
	
	public JTextArea gettSaisieMessage(){
		return tSaisieMessage;
	}
	
	//initialise la liste des utilisateurs connectes et prepare son affichage 
	public void InitListConnectedUsers(DefaultListModel<RemoteUsers> listConnectedUsers){
		this.listConnectedUsers=listConnectedUsers;
		initJlistAndHashTable();
	}
	
	public void addController(Controller controller){
		bDisconnect.addActionListener(controller);
		bSendMessage.addActionListener(controller);
		bSelectFile.addActionListener(controller);
		bOpenFile.addActionListener(controller);
	}

	
	private void init(){
		this.setSize(1200, 670);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		
		lConnectedUsers = new JLabel("Utilisateurs connectés :") ; 
		lConnectedUsers.setAlignmentX(Component.CENTER_ALIGNMENT);
				
		bDisconnect = new JButton("Deconnexion");
		bDisconnect.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bSendMessage = new JButton("Envoyer");
		bSendMessage.setEnabled(false);
		bSendMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bSelectFile = new JButton("Selectionner fichier");
		bSelectFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bOpenFile = new JButton("Ouvrir Fichier Recu") ; 
		bOpenFile.setEnabled(false);
		bOpenFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		tSaisieMessage = new JTextArea();
		tSaisieMessage.setLineWrap(true);
		tSaisieMessage.setWrapStyleWord(true);
		tSaisieMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setMaximumSize(new Dimension(1200, 670));
		mainPanel.setMinimumSize(new Dimension(1200, 670));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		topPanel.setMaximumSize(new Dimension(1200, 550));
		topPanel.setMinimumSize(new Dimension(1200, 550));

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.setMaximumSize(new Dimension(1200, 90));
		bottomPanel.setMinimumSize(new Dimension(1200, 90));
		
		JPanel userslistPanel = new JPanel();
		userslistPanel.setLayout(new BoxLayout(userslistPanel, BoxLayout.PAGE_AXIS));
		userslistPanel.setMaximumSize(new Dimension(270, 550));
		userslistPanel.setMinimumSize(new Dimension(270, 550));
		
		listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.LINE_AXIS));
		listPanel.setMaximumSize(new Dimension(270, 550));
		listPanel.setMinimumSize(new Dimension(270, 550));
		
		JPanel conversationPanel = new JPanel();
		conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.PAGE_AXIS));
		conversationPanel.setMaximumSize(new Dimension(885, 550));
		conversationPanel.setMinimumSize(new Dimension(885, 550));
		
		textConversationPanel = new JPanel();
		textConversationPanel.setLayout(new BoxLayout(textConversationPanel, BoxLayout.LINE_AXIS));
		textConversationPanel.setMaximumSize(new Dimension(875, 535));
		textConversationPanel.setMinimumSize(new Dimension(875, 535));
		
		JPanel disconnetPanel = new JPanel();
		disconnetPanel.setLayout(new BoxLayout(disconnetPanel, BoxLayout.PAGE_AXIS));
		disconnetPanel.setMaximumSize(new Dimension(270, 90));
		disconnetPanel.setMinimumSize(new Dimension(270, 90));
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.LINE_AXIS));
		messagePanel.setMaximumSize(new Dimension(885, 90));
		messagePanel.setMinimumSize(new Dimension(885, 90));
		
		JPanel saisieMessagePanel = new JPanel();
		saisieMessagePanel.setLayout(new BoxLayout(saisieMessagePanel, BoxLayout.PAGE_AXIS));
		saisieMessagePanel.setMaximumSize(new Dimension(700, 90));
		saisieMessagePanel.setMinimumSize(new Dimension(700, 90));
		
		JPanel boutonMessagePanel = new JPanel();
		boutonMessagePanel.setLayout(new BoxLayout(boutonMessagePanel, BoxLayout.PAGE_AXIS));
		boutonMessagePanel.setMaximumSize(new Dimension(180, 90));
		boutonMessagePanel.setMinimumSize(new Dimension(180, 90));
	
		userslistPanel.add(lConnectedUsers);
		userslistPanel.add(listPanel);

 		saisieMessagePanel.add(tSaisieMessage);
		boutonMessagePanel.add(bSelectFile);
		boutonMessagePanel.add(bSendMessage);
		boutonMessagePanel.add(bOpenFile);
		disconnetPanel.add(bDisconnect);
		
		conversationPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		conversationPanel.add(textConversationPanel);
		
		messagePanel.add(saisieMessagePanel);
		messagePanel.add(Box.createRigidArea(new Dimension(15, 0)));
		messagePanel.add(boutonMessagePanel);
		topPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		topPanel.add(userslistPanel);
		topPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		topPanel.add(conversationPanel);
		topPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		bottomPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		bottomPanel.add(disconnetPanel);
		bottomPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		bottomPanel.add(messagePanel);
	    mainPanel.add(topPanel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	    mainPanel.add(bottomPanel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	  
		this.getContentPane().add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//non visible par defaut
		this.setVisible(false);
	}
	
	//initialise l'affichage de la liste des utilisateurs connectes
	public void initJlistAndHashTable(){
		htTConversation = new Hashtable<String,JTextArea>();
		htScrollBarConversation = new Hashtable<String,JScrollPane>();
		//La Jlist est associee a notre liste d'utilisateurs connectes 
		jlListeConnectedUsers = new JList<RemoteUsers>(this.listConnectedUsers);
		jlListeConnectedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlListeConnectedUsers.setMaximumSize(new Dimension(260, 550));
		jlListeConnectedUsers.setMinimumSize(new Dimension(260, 550));
		JScrollPane scrollBarListe = new JScrollPane(jlListeConnectedUsers);
		scrollBarListe.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBarListe.setMinimumSize(new Dimension(20, 550));
		scrollBarListe.setMaximumSize(new Dimension(20, 550));
		//Le mouse listener associe a la Jlist devient cette classe
		jlListeConnectedUsers.addMouseListener(this);
		
		listPanel.add(jlListeConnectedUsers);
		listPanel.add(scrollBarListe);
		
		//Si la liste des utilisateurs connectes est vide
		//Par exemple on est le premier utilisateur du chat
		if (this.listConnectedUsers.isEmpty()){
			//l'utilisateur selectionnne est un utilisateur imaginaire
			//On luit cree une scroll bar et une zone de texte pour la conversation
			//Celles ci ne sont pas visible
			selectedUser="";
			htTConversation.put(selectedUser,new JTextArea());
			htTConversation.get(selectedUser).setVisible(false);
			htScrollBarConversation.put(selectedUser,new JScrollPane(htTConversation.get(selectedUser)));
			htScrollBarConversation.get(selectedUser).setVisible(false);
		}
		else {
			//On cree aussi un utilisateur imaginaire au cas ou 
			//la liste viendrait a devenir vide
			selectedUser="";
			htTConversation.put(selectedUser,new JTextArea());
			htTConversation.get(selectedUser).setVisible(false);
			htScrollBarConversation.put(selectedUser,new JScrollPane(htTConversation.get(selectedUser)));
			htScrollBarConversation.get(selectedUser).setVisible(false);
			//on cree une scroll bar et une de zone de texte pour la conversation pour chaque 
			//utilisateurs connectes de la liste
			int i ;
			for (i=0;i<listConnectedUsers.size();i++){
				htTConversation.put(listConnectedUsers.getElementAt(i).getUsername(),new JTextArea());
				htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setLineWrap(true);
				htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setWrapStyleWord(true);
				htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setAlignmentX(Component.CENTER_ALIGNMENT);
				htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setEditable(false);
				htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setVisible(false);
				htScrollBarConversation.put(listConnectedUsers.getElementAt(i).getUsername(),new JScrollPane(htTConversation.get(listConnectedUsers.getElementAt(i).getUsername())));
				htScrollBarConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				htScrollBarConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setMinimumSize(new Dimension(10, 550));
				htScrollBarConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setMaximumSize(new Dimension(10, 550));	
				htScrollBarConversation.get(listConnectedUsers.getElementAt(i).getUsername()).setVisible(false);
				textConversationPanel.add(htTConversation.get(listConnectedUsers.getElementAt(i).getUsername()));
				textConversationPanel.add(htScrollBarConversation.get(listConnectedUsers.getElementAt(i).getUsername()));
			}
		}
	}
	
	public void removeListConnectedUsers(){
		this.listConnectedUsers.clear();
		this.listPanel.removeAll();		
		this.listPanel.setVisible(true);
		this.htScrollBarConversation.clear();
		this.htTConversation.clear();
	}
	
	//supprime un utilisateur a partir de son username
	public void removeUser(String username){
		int i=0;
		boolean sup =false;
		while (i<this.listConnectedUsers.size() && sup==false){
			if (this.listConnectedUsers.getElementAt(i).getUsername().equals(username)){
				this.listConnectedUsers.remove(i);
				sup=true;
			}
			i++;
		}
		//si l'utilisateur a supprime a ete trouve
		if (sup==true){
			htTConversation.remove(username).setVisible(false);
			htScrollBarConversation.remove(username).setVisible(false);
			//si l'utilisateur supprimer est l'utilisateur actuellement 
			//selectionne, l'utilisateur selectionne devient l'
			//utilisateur imaginaire
			if (username.equals(selectedUser)){
				selectedUser="";
			}
			if(listConnectedUsers.size()==0) bSendMessage.setEnabled(false); 
		}
	}

	//ajoute un utilisateur a notre liste d'utilisateurs connectes
	public void addUser(String username){
		//ajoute une zone de texte pour converser et une scroll bar 
		//pour le nouvel utilisateur
		htTConversation.put(username,new JTextArea());
		htTConversation.get(username).setLineWrap(true);
		htTConversation.get(username).setWrapStyleWord(true);
		htTConversation.get(username).setAlignmentX(Component.CENTER_ALIGNMENT);
		htTConversation.get(username).setEditable(false);
		htTConversation.get(username).setVisible(false);
		
		htScrollBarConversation.put(username,new JScrollPane(htTConversation.get(username)));
		htScrollBarConversation.get(username).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		htScrollBarConversation.get(username).setMinimumSize(new Dimension(10, 550));
		htScrollBarConversation.get(username).setMaximumSize(new Dimension(10, 550));	
		htScrollBarConversation.get(username).setVisible(false);
		
		textConversationPanel.add(htTConversation.get(username));
		textConversationPanel.add(htScrollBarConversation.get(username));
		//On l'ajoute à la liste des utilisateurs connectés et donc à la JList
		this.listConnectedUsers.addElement(new RemoteUsers(username,0));
	}
	
	//fonction ecrivant un message recu sur la zone de conversation de l'utilisateur 
	//indique
	public void writeReceivedMessage(String message, String username){
		//on incremente le nombre de messages recus de l'utilisateur recepteur
		int i=0;
		boolean trouve = false ;
		while (i<this.listConnectedUsers.size() && trouve==false){
			if (this.listConnectedUsers.getElementAt(i).getUsername().equals(username)){
				trouve=true;
				this.listConnectedUsers.getElementAt(i).setNbMessageReceived(this.listConnectedUsers.getElementAt(i).getNbMessageReceived()+1);
			}
			i++;
		}
		//pour mettre a jour la Jlist et afficher nos modifications
		this.jlListeConnectedUsers.setModel(listConnectedUsers);
		htTConversation.get(username).setText(htTConversation.get(username).getText()+"\n"+username+" a envoyé :"+"\n       "+message+"\n");
	}
	
	//fonction ecrivant un message envoye sur la zone de conversation de l'utilisateur 
	//indique
	public void writeSentMessage(String message, String username){
		htTConversation.get(username).setText(htTConversation.get(username).getText()+"\nVous avez envoyé :"+"\n       "+message+"\n");
	}
	
	//defini les actions a realiser lorsqu'on selectionne un utilisateur 
	// de la Jlist
	public void mouseClicked(MouseEvent e) {
		//si aucun utilisateur est selectionne on desactive le bouton d'envoi
		if (selectedUser.equals("")){
			bSendMessage.setEnabled(true);
		}
		//on cache la zone de conversation et la scroll bar de l'utilisateur connecte
		//car un nouvel utilisateur vient d'etre selectionne
		htTConversation.get(selectedUser).setVisible(false);
		htScrollBarConversation.get(selectedUser).setVisible(false);
		if (listConnectedUsers.isEmpty()==false){
			//on recupere l'username de l'utilisateur selectionne et on affiche sa zone de 
			//conversation et sa scroll bar
			String nom = listConnectedUsers.getElementAt(jlListeConnectedUsers.locationToIndex(e.getPoint())).getUsername();
			htTConversation.get(nom).setVisible(true);
			htScrollBarConversation.get(nom).setVisible(true);
			this.setVisible(true);
			//il devient l'utilisateur selectionne
			selectedUser=nom;
			//on initialise a 0 son nombre de message recu
			int i=0;
			boolean trouve = false ;
			while (i<this.listConnectedUsers.size() && trouve==false){
				if (this.listConnectedUsers.getElementAt(i).getUsername().equals(nom)){
					trouve=true;
					this.listConnectedUsers.getElementAt(i).setNbMessageReceived(0);
				}
				i++;
			}
		}
	}
	
	//fonction permettant d'ouvrir une fenetre de sauvegarde de fichier
	public File openFileSave() {
		fileSave= new JFileChooser() ; 
		fileSave.showSaveDialog(null) ; 
		File f = fileSave.getSelectedFile() ; 
		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.flush(); 
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileSave.getSelectedFile() ;
	}
	
	public void setEnableOpenFileSave(boolean b) {
		bOpenFile.setEnabled(b);
	}
	
	//fonction permettant d'ouvrir une fenetre de selection de fichier
	public File openFileChooser() {
		fileChooser=new JFileChooser() ;
		fileChooser.showOpenDialog(null) ;
		return fileChooser.getSelectedFile() ; 
	}
	
	public void displayChatWindows(){
		this.setVisible(true);
	}
	
	public void closeChatWindows(){
		this.dispose();
	}
	
	public void launchDeconnexionPopUp(){
		JOptionPane.showMessageDialog(null, "Vous avez été déconnecté.","Déconnexion",JOptionPane.PLAIN_MESSAGE);	
		//System.exit(0);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
