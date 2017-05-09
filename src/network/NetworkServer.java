package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class NetworkServer extends Thread{
	
	/* ############################
	  
	  Classe NetworkServer permettant de recevoir
	  des donnees sur le reseau. C'est un 
	  Thread qui recupere des paquet sur le reseau
	  par le biais d'une socket et qui les mets
	  dans sa fifo
	  
	   ############################*/

	final static int SIZE_MAX = 1024 ; 
	private DatagramSocket server ; 
	private int portServer ;
	private BlockingQueue<DatagramPacket> fifo ; 
	
	public NetworkServer(int portServer,BlockingQueue<DatagramPacket> fifo) {
		this.fifo=fifo ; 
		this.portServer = portServer;
		try {
			server=new DatagramSocket(portServer);
			server.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}

	public int getPortServer() {
		return portServer;
	}
	
	public void closeSocket() {
		this.server.close();
	}


	@Override
	//fonction de lancement du thread
	public void run() {
		
		while(true) {
			//bloquant 
			try {
				//recupere un paquet sur le reseau
				DatagramPacket p ; 
				byte [] buf ;
				buf = new byte[SIZE_MAX] ;
				p=new DatagramPacket(buf, SIZE_MAX) ; 
				server.receive(p);
				//ajoute le paquet recu a sa fifo
				fifo.add(p) ;				
			} catch (IOException  e) {
				return  ;
			}
		}
	}

}
