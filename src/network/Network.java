package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import lib.Pair;
import model.Message;

//size max 1024

public class Network extends Observable implements Runnable{
	
	/* ############################
	  
	  Classe Network permettant d'envoyer
	  et recevoir des donnees sur le reseau
	  Cette classe etends la classe observable
	  pour qu'un observer soit notifie lorsqu'
	  un message est recu. 
	  
	   ############################*/
	
	//La classe Network contient principalement un serveur
	//de reception, un serveur d'envoi, une fifo de
	// reception et une fifo d'envoi 
	private NetworkClient client ; 
	private NetworkServer server ; 
	private BlockingQueue<DatagramPacket> fifoClient ; 
	private BlockingQueue<DatagramPacket> fifoServer ;
	private Thread t ; 
	private int portDistant ; 

	
	public Network(int portServer,int portDistant) {
		fifoClient = new ArrayBlockingQueue<DatagramPacket>(10)  ;
		fifoServer = new ArrayBlockingQueue<DatagramPacket>(10) ;
		server= new NetworkServer(portServer,fifoServer) ; 
		client = new NetworkClient(fifoClient) ;
		this.portDistant=portDistant; 
	}
	
	public void launchServer() {
		server.start(); 
	}
	
	public void launchClient() {
		client.start(); 
	}
	
	//fonction d'envoi d'un objet Message a destination d'une
	//adresse ip precise
	public void send(Message m,InetAddress addr) {
		ByteArrayOutputStream bos ; 
		ObjectOutput out ;
		byte [] buf; 
		DatagramPacket packet ; 
		try {
			//on converti l'objet Message en tableau d'octet
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject((Message)m);
			out.flush();
			buf=bos.toByteArray();
			//creation du paquet a envoyer
			packet = new DatagramPacket(buf,buf.length) ;
			packet.setAddress(addr);
			packet.setPort(portDistant);
			//on ajoute le message a envoyer a la fifo d'envoi
			fifoClient.add(packet) ;
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//fonction qui recupere le dernier message recu
	public Pair<Message,InetAddress> getLastMessage()  {
		ByteArrayInputStream bis ; 
		ObjectInput in ;
		Message m = null ; 
		DatagramPacket packet ; 
		Pair<Message,InetAddress> p  = null;
		try {
			//on recupere le paquet recu et on recree l'objet 
			//Message à partir du tableau d'octet
			packet = fifoServer.take() ;
			bis = new ByteArrayInputStream((packet).getData());
			in = new ObjectInputStream(bis);
			m = ((Message)in.readObject()) ;
			p = new Pair<Message,InetAddress>(m,packet.getAddress()) ; 
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			return null ; 
		}
		return p ; 
	}
	
	
	//fonction qui retroune l'adresse Ip de notre machine sur le reseau
	public static InetAddress getIp() throws UnknownHostException {
		return InetAddress.getLocalHost() ; 
	}
	
	//fonction qui lance l'obervable
	public void run() {
		t=Thread.currentThread() ; 
		while(true){
			Pair<Message,InetAddress> message = getLastMessage() ;
			if(message==null) return ; 
			//si le dernier message recu n'est pas vide on notifie l'
			//observer et on lui envoie le message
			setChanged();
            notifyObservers(message);
		}
	}
	
	public void stopNetwork(){
		this.server.closeSocket();
		this.server.interrupt();
		this.client.interrupt();
		if(t!=null) t.interrupt();;
	}

}
