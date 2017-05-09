package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class NetworkClient extends Thread{
	
	/* ############################
	  
	  Classe NetworkClient permettant d'envoyer
	  des donnees sur le reseau. C'est un 
	  Thread qui envoie sur le reseau par le biais
	  d'une socket les paquets mit dans sa fifo
	  
	   ############################*/
	
	private DatagramSocket client ;
	private BlockingQueue<DatagramPacket> fifo ;  

	public NetworkClient(BlockingQueue<DatagramPacket> fifo) {
		this.fifo=fifo ; 
		try {
			client=new DatagramSocket() ;
			client.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}

	@Override
	//fonction de lancement du thread
	public void run() {
		while(true) {
			try {
				//on recupere le paquet de la fifo et on l'envoie 
				//sur la socket
				DatagramPacket packet ; 
				//take est bloquant
				packet = fifo.take() ; 
				//send
				client.send(packet);				 
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return ; 
			}
		}
	} 
	


}
