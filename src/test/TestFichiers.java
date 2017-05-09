package test;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gui.Vue;
import lib.Pair;
import model.Model;
import model.MsgFile;
import model.MsgHello;
import network.Network;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestFichiers {
	
	static Network n ; 
	static ControllerSansPopUp c ; 
	File f ; 

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
	public void test1SerializationFileSend() {
		f = new File("toto")  ;
		try {
			n.send(new MsgHello("Deuxieme", "Premier", false,true), InetAddress.getLocalHost());
			Thread.sleep(500);
			n.send(new MsgFile("Deuxieme", "Premier", f), InetAddress.getLocalHost());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2ReceiveFileTypeOK() {
		try {
			Network n2 = new Network(1236,1237) ;
			n2.launchServer();
			n2.launchClient();
			Network n3 = new Network(1237,1236) ;
			n3.launchServer();
			n3.launchClient();
			n2.send(new MsgFile("Troisieme", "Quatrieme", f), InetAddress.getLocalHost());
			Pair<?,?> p = n3.getLastMessage() ;
			Assert.assertTrue(p.getE() instanceof MsgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void test3ReceptionFile() {
		try {
			File ff = c.getFile() ; 
			FileInputStream isf = new FileInputStream(f) ; 
			FileInputStream isff = new FileInputStream(ff) ; 
			
			byte[] bufF = new byte[1024] ; 
			byte[] bufFF = new byte[1024] ; 
			while(isf.read(bufF)>0) {
				Assert.assertArrayEquals(bufF, bufFF);
			}
			
			isf.close();
			isff.close();
			
			f.delete() ; 
			ff.delete() ; 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
