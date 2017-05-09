package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class TestModel {
	
	static Network n ; 
	static ControllerSansPopUp c ; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		c = new ControllerSansPopUp(new Model(),new Network(1234, 1235), new Vue()); 
		n = new Network(1235,1234) ; 
		n.launchServer();
		n.launchClient();
		c.connexion("Premier");
		Thread.sleep(500);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test0CheckAddConnectedUser() {
		try {
			n.send(new MsgHello("Zero","Premier",false,true), InetAddress.getLocalHost());
			Thread.sleep(1000);
			Assert.assertFalse(c.getModel().checkUsernameNotExists("Zero"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test1CheckRemoveConnectedUser() {
		try {
			n.send(new MsgGoodbye("Zero","Premier"), InetAddress.getLocalHost());
			Thread.sleep(1000);
			Assert.assertTrue(c.getModel().checkUsernameNotExists("Zero"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2CheckUserConnectedOk() {
		try {
			n.send(new MsgHello("Deuxieme","Premier", false, true), InetAddress.getLocalHost());
			Pair<?,?> p = n.getLastMessage() ;
			//ATTENTE D'UN CHECK
			while(true) {
				if(p.getE() instanceof MsgCheck) {
					if(((MsgCheck) p.getE()).getContent()==false) {
						n.send(new MsgCheck("Deuxieme","Premier", true), InetAddress.getLocalHost());
						break ; 
					}
				}
				p = n.getLastMessage() ; 
			}
			Thread.sleep(3000); //ATTENTE DE LA RECEPTION DU CHECK_OK PAR L'USER
			Assert.assertFalse(c.getModel().checkUsernameNotExists("Deuxieme")) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========    ATTENTION        ===========//
	//===========  TEST COMMENTE ===========//  
	//===========    CAR LONG     ===========//    
//	/**
//	 * 
//	 * ATTENTION TEST LONG (ENVIRON 1min) car on doit attendre la réception d'un message check 
//	 * 	or comme on se connecte alors qu'un check a été envoyé avant il faut attendre davantage pour en recevoir un
//	 * 
//	 */
	
/*	@Test
	public void test3CheckUserConnectedNotOk() {
		try {
			n.send(new MsgHello("Troisieme","Premier", false, true), InetAddress.getLocalHost());
			Thread.sleep(10000); //ATTENTE DE LA RECEPTION DU HELLO PAR L'USER
			Assert.assertFalse(c.getModel().checkUsernameNotExists("Troisieme")) ;
			Thread.sleep(67000); //ATTENTE DE LA RECEPTION D'UN CHECK 
			Assert.assertTrue(c.getModel().checkUsernameNotExists("Troisieme")) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * 
	 * Check si après le goodbye d'un inconnu, la liste des utilisateurs n'a pas été modifié 
	 * 
	 */
	@Test
	public void test4CheckGoodbyeInconnu() {
		try {
			int size = c.getModel().getListConnectedUsers().size() ; 
			n.send(new MsgGoodbye("Inconnu","Premier"), InetAddress.getLocalHost());
			Thread.sleep(1000);
			int size2 = c.getModel().getListConnectedUsers().size() ;
			Assert.assertTrue(size==size2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
