package controller;
import java.net.InetAddress;
import java.net.UnknownHostException;

import gui.Vue;
import model.Contact;
import model.Model;
import model.MsgGoodbye;
import model.MsgHello;
import model.MsgTxt;
import network.Network;

public class TestReceive {
		
	public static void main(String [] args) {
		try {
			Network n = new Network(1234,1235);
			Vue vue= new Vue();
			Model model = new Model();

			Controller ctrl= new Controller(model,n,vue);
			while(ctrl.getConnected()==0){
				System.out.println(ctrl.getConnected());
			}
			n.send(new MsgHello("Jean","Broadcast",false,true),InetAddress.getByName("127.0.0.1"));
			n.send(new MsgHello("Jean2","Broadcast",false,true),InetAddress.getByName("127.0.0.1"));
			n.send(new MsgHello("Jean3","Broadcast",false,true),InetAddress.getByName("127.0.0.1"));
			n.send(new MsgHello("Jean4","Broadcast",false,true),InetAddress.getByName("127.0.0.1"));
			Thread.sleep(20000);
			n.send(new MsgGoodbye("Jean3","Broadcast"),InetAddress.getByName("127.0.0.1"));
			} catch (UnknownHostException | InterruptedException e1) {
			e1.printStackTrace();
		}	
	}


	
}
