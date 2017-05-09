package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import lib.Pair;
import model.Message;
import model.MessageType;
import model.MsgCheck;

public class CheckThread extends Thread{
	

	/* ############################
	  
	  Classe tournant en arrière plan
	  permettant d'envoyer des "check" 
	  et de recevoir des reponses "check_ok". 
	  Permet de mettre à jour la
	  liste d'utilisateurs connectes
	  
	   ############################*/
	
	private Controller ctrl ; 
	private int timerState ;
	private BlockingQueue<Pair<Message,InetAddress>> fifo ; 
	private boolean newConnection ; 
	private Timer t ; 
	public Thread tr ; 
	
	public CheckThread(Controller ctrl,BlockingQueue<Pair<Message,InetAddress>> fifo) {
		this.ctrl=ctrl ; 
		this.fifo=fifo ; 
		this.newConnection=false ; 
	}
	
	public boolean getNewConnection() {
		return this.newConnection ; 
	}
	
	public void setNewConnection(boolean b) {
		this.newConnection=b; 
	}

	public int getTimerState() {
		return timerState;
	}

	public void setTimerState(int timerState) {
		this.timerState = timerState;
	}

	@Override
	public void run() {
		tr=Thread.currentThread(); 
		while(true) {
			//on uncheck tous les utilisateurs
			this.ctrl.getModel().uncheckAllUsers();
			setTimerState(0);
			//on envoie un message "check" en broadcast
			try {
				Message m = this.ctrl.getModel().createMessage(ctrl.getModel().getMySlef().getUsername(),this.ctrl.getBroadcast(),MessageType.CHECK, false, false) ; 
				this.ctrl.getNetwork().send(m,InetAddress.getByName(this.ctrl.getBroadcast()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}			
			//on lance un timer de 20secondes
			t = new Timer() ; 
			t.schedule(new TimerCheck(t,this), 20000);
			//tant que le timer n'est pas ecoule 
			while(this.getTimerState()==0) {
				Pair<Message,InetAddress> p = null ; 
				try {
					synchronized(fifo) {
						//tant qu'un signal n'est pas recu (c'est a dire 
						//qu'aucun message n'a ete recu), on attend
						fifo.wait();
						if(this.getTimerState()==0) {
							//on recupere le message recu
							p=fifo.take() ;
							Message m = p.getE() ; 
							if(m.getType()==MessageType.CHECK){	
								//si le message est un "check_ok" 
								if (((MsgCheck)m).getContent()==true) {
									//on check la source du check_ok
									ctrl.getModel().getConnectedUser(m.getUserSrc()).setChecked(true);
								}
							}
						}
					}
				} 
				catch (InterruptedException | IOException e) {} 
			}
			//A cet endroit, le timer est fini
			//S'il n'y a pas de nouvelle connexion on supprime les utilisateurs non checked
			if(this.getNewConnection()==false) {
				this.ctrl.getModel().removeUncheckedUsers();
				this.ctrl.getVue().updateConnectedUsers(this.ctrl.getModel().convertListToString());
			} else {
				this.setNewConnection(false);
			}
		}
		
	}	
	
	public void stopCheckThread() {
		//annule le timer
		this.t.cancel();
		//stop le thread 
		tr.interrupt();
	}
	
	//Classe du timer
	class TimerCheck extends TimerTask {	
		private Timer timer ; 
		private CheckThread check ; 
		
		public TimerCheck(Timer timer, CheckThread check) {
			this.check=check ; 
			this.timer=timer ; 
		}
		//execution du timer 
		public void run(){
			synchronized(fifo) {
				//on envoie un signal pour eviter d'être bloque par la fifo
				//on met ensuite l'etat du timer a 1, le timer est donc ecoule
				fifo.notify();
				check.setTimerState(1);
				timer.cancel();
			}
		}	
	}
	
	
	
}
