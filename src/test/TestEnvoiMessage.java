package test;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gui.Vue;
import lib.Pair;
import model.Model;
import model.MsgCheck;
import model.MsgGoodbye;
import model.MsgHello;
import network.Network;





@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEnvoiMessage {

	static ControllerSansPopUp c1 ; 
	static Network n ; 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		c1 = new ControllerSansPopUp(new Model(),new Network(1234,1235),new Vue())  ;  
		n = new Network(1235,1234) ; 
		n.launchServer();
		n.launchClient();
		c1.connexion("Premier");
	}

	@Before
	public void setUp() throws Exception {
	}
	


	@Test
	public void test1EnvoiMessageHello() {
		Pair<?,?> p = n.getLastMessage() ;
		MsgHello m = (MsgHello) p.getE() ; 
		Pair<Boolean,Boolean> hello = m.getContent() ; 
		Assert.assertFalse(hello.getE());
		Assert.assertTrue(hello.getT());
	}
	
	
	//ATTENTION TEST LONG
	@Test
	public void test2EnvoiMessageCheckApresConnexion() {
		Pair<?,?> p = n.getLastMessage() ;
		MsgCheck m = (MsgCheck) p.getE() ; 
		boolean check = m.getContent() ; 
		Assert.assertFalse(check); //check d'un check  
		p = n.getLastMessage() ;
		m = (MsgCheck) p.getE() ; 
		check = m.getContent() ; 
		Assert.assertFalse(check); //check d'un autre check 
		
	}
	
	
	@Test
	public void test3EnvoiMessageGoodbyeDeconnexion() {
		c1.deconnexion();
		Pair<?,?> p = n.getLastMessage() ;
		MsgGoodbye m = (MsgGoodbye) p.getE() ; 
		Assert.assertTrue(m instanceof MsgGoodbye); 
	}
	
	/**
	 * 
	 * Teste s'il n'y a pas de check envoyé après la deconnexion, on créé un timer pour que l'on ne soit pas
	 * Indéfiniement bloqué dans la récupération d'un message qui n'arrivera pas 
	 * 
	 */
	@Test 
	public void test4VerifPasDeCheckApresDeconnexion(){
		new Thread() {
			public Thread t ; 
			public Pair<?,?> m =null; 
			@Override
			public void run() {
				final Timer tim = new Timer() ; 
				tim.schedule(new TimerTask() {
					@Override
					public void run() {
						tim.cancel();
						stopThread();
						Assert.assertTrue(m==null);
					}
				}, 22000); 
				t=Thread.currentThread() ;
				m = n.getLastMessage() ;
			}
			public void stopThread() {
				t.interrupt();
			}
		}.start();
	}
	
	
	

}
