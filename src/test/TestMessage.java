package test;

import java.net.UnknownHostException;

import org.junit.Assert;

import lib.Pair;
import model.Message;
import model.MsgHello;
import model.MsgTxt;
import network.Network;

public class TestMessage {

	static Network n1 ;
	static Network n2 ; 
	static final String data = "coucou"; 
	
	@org.junit.BeforeClass
	public static void  setUpBeforeClass() {
			n1 = new Network(1234,1235) ;
			n2 = new Network(1235,1234) ; 
			
			n1.launchClient();
			n2.launchServer();
			
	}
	
	@org.junit.Before
	public void setUp() {
		try {
			Message m = new MsgTxt("toto","titi",data) ; 
			n1.send(m,Network.getIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@org.junit.AfterClass
	public static void tearDownAfterClass() {
		n1=null; 
		n2=null ; 
	}
	
	/**
	 * Test si le message se serialize bien et s'envoie
	 */
	@org.junit.Test
	public void testSerializationMessageEtSend() {
		try {
			Message m = new MsgTxt("toto","titi",data) ; 
			n1.send(m,Network.getIp());
		} catch (UnknownHostException  e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Regarde si le paquet reçu est bien un message
	 */
	@org.junit.Test
	public void testReceivePacket() {
			Pair<?,?> p = n2.getLastMessage() ;
			Assert.assertTrue(p.getE() instanceof Message);
	}
	
	/**
	 * Regarde si le message reçu est bien un message texte 
	 */
	@org.junit.Test
	public void testReceiveMessage() {
			Pair<?,?> p = n2.getLastMessage() ;
			Assert.assertTrue(p.getE() instanceof MsgTxt);
	}
	
	/**
	 * Regarde si le message reçu n'est pas un message Hello au lieu d'un message texte 
	 */
	@org.junit.Test
	public void testReceiveMessageTypeOk() {
			Pair<?,?> p = n2.getLastMessage() ;
			Assert.assertFalse(p.getE() instanceof MsgHello);
	}
	
	/**
	 * Regarde si le content du message reçu est le même que celui envoyé 
	 */
	@org.junit.Test
	public void testContentReceiveMessageText() {
			Pair<?,?> p = n2.getLastMessage() ;
			Assert.assertTrue(((MsgTxt)p.getE()).getContent().equals(data));
	}
	
	
	
	
	

}
