package test;

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
import model.MsgHello;
import network.Network;




@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestReceptionMessage {

	static ControllerSansPopUp c1 ; 
	static ControllerSansPopUp c2 ; 
	static Network n ; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		c1 = new ControllerSansPopUp(new Model(),new Network(1234,1235),new Vue())  ;  
		n = new Network(1235,1234) ; 
		n.launchServer();
		n.launchClient();
		c1.connexion("Premier");
		Thread.sleep(500);
		n.getLastMessage() ; //la reception du hello de premier 
	}

	@Before
	public void setUp() throws Exception {

	}


	@Test
	public void test1ReponseHelloOK() {
		try {
			n.send(new MsgHello("Deuxieme", "Premier", false, true), InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Pair<?,?> p = n.getLastMessage() ;
		while(!(p.getE() instanceof MsgHello)) { //tant qu'on reçoit autre chose que des messages hello, c'est à dire des checks ici 
			p=n.getLastMessage() ; 
		}
		
		Assert.assertTrue(((MsgHello)p.getE()).getContent().getE()&&((MsgHello)p.getE()).getContent().getT());
	}
	
	@Test
	public void test2ReponseHelloNotOK() {
		try {
			n.send(new MsgHello("Premier", "Premier", false, true), InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Pair<?,?> p = n.getLastMessage() ;
		while(!(p.getE() instanceof MsgHello)) { //tant qu'on reçoit autre chose que des messages hello, c'est à dire des checks ici 
			p=n.getLastMessage() ; 
		}
		Assert.assertTrue(((MsgHello)p.getE()).getContent().getE()&&!((MsgHello)p.getE()).getContent().getT());
	}

	@Test 
	public void test3CheckEnvoiCheckOK() {
		try {
			n.send(new MsgCheck("Deuxieme","Premier", false), InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Pair<?,?> p = n.getLastMessage() ; 
		while(true) {
			if(p.getE() instanceof MsgCheck) {
				if(((MsgCheck) p.getE()).getContent()==true) {
					break ; 
				}
			}
			p = n.getLastMessage() ; 
		}
		
	}
	
	@Test 
	public void test4CheckEtatConnectionApresHelloNotOk() {
		n.stopNetwork();
		c2 = new ControllerSansPopUp(new Model(),new Network(1235,1234),new Vue()) ; 
		c2.connexion("Premier");
		Assert.assertTrue(c2.getConnected()==0);
	}
	

}
