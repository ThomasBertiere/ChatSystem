package test;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import controller.Controller;
import gui.RemoteUsers;
import gui.Vue;
import model.Model;
import model.MsgHello;
import network.Network;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConnexionDeconnexion {

	

	
	

	/**
	 * Test de la connexion / on est connecté pour le controleur (boolean connected) 
	 */
	@Test
	public void test1Connexion() {
		ControllerSansPopUp c = new ControllerSansPopUp(new Model(), new Network(1234,1234),new Vue()) ; 
		c.connexion("test");
		Assert.assertTrue(c.getConnected()==1);
		c.deconnexion();
	}
	
	/**
	 * Test de la deconnexion : est ce connecté est vrai ? 
	 * 						  
	 */
	@Test
	public void test2Deconnexion() {
		ControllerSansPopUp c = new ControllerSansPopUp(new Model(), new Network(1234,1234),new Vue()) ; 
		c.connexion("test");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.deconnexion(); 
		Assert.assertTrue(c.getConnected()==0);
	}
	
	/**
	 * Test de reconnexion, on peut se reconnecter une fois deconnecte
	 */
	@Test
	public void test3Reconnexion(){
		ControllerSansPopUp c = new ControllerSansPopUp(new Model(), new Network(1234,1234),new Vue()) ; 
		c.connexion("test");
		try {
			Thread.sleep(1000);
			Assert.assertTrue(c.getConnected()==1);
			Thread.sleep(1000);
			c.deconnexion();
			Thread.sleep(1000);
			Assert.assertTrue(c.getConnected()==0);
			ControllerSansPopUp c2 = new ControllerSansPopUp(new Model(), new Network(1234,1234),new Vue()) ; 
			c2.connexion("test");
			Thread.sleep(1000);
			Assert.assertTrue(c2.getConnected()==1);
			c2.deconnexion();
			Assert.assertTrue(c2.getConnected()==0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * On peut se reconnecte si on recoit un Hello_Not_Ok 
	 * lors de la connexion
	 */
	@Test 
	public void test4ReconnexionHelloNotOk(){
		Network reseau = new Network(1234,1234);
		ControllerSansPopUp c = new ControllerSansPopUp(new Model(), reseau,new Vue()) ; 
		//on l'envoie avant sinon il le recoit pas durant la connexion
		reseau.send(new MsgHello("userDistant","test",true,false), InetAddress.getLoopbackAddress());
		c.connexion("test");
		Assert.assertTrue(c.getConnected()==0);
		c.connexion("test");
		try {
			Thread.sleep(1000);
		}
		catch  (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(c.getConnected()==1);
		c.deconnexion();
	}
	


}
