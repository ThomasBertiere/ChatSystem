import controller.Controller;
import gui.Vue;
import model.Model;
import network.Network;

public class Main {

	/* ############################
	  
	  Classe permettant de lancer le
	  ChatSystem
	  
	   ############################*/
	
	private Controller controler; 
			
	public Main(){
		this.controler=new Controller(new Model(),new Network(1234,1235),new Vue());
		//this.controler=new Controller(new Model(),new Network(1235,1234),new Vue());
	}

	public static void main(String[] args){
		Main m=new Main() ;	
	}

}
