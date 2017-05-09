package model;

import lib.Pair;

public class MsgHello extends Message{
	
	/* ############################
	  
	  Classe representant les messages
	  de connexion "Hello". Un hello est
	  envoye lors d'une connexion. Un 
	  Hello_Ok est retourne si la connexion
	  ne cause pas de probleme. Sinon un 
	  Hello_not_ok est retourne
	 
	  	HELLO        -> Ack = 0 ; Connect = 1 ; 
		HELLO_OK     -> Ack = 1 ; Connect =1 ; 
	 	HELLO_NOT_OK -> Ack = 1 ; Connect =0 ; 


	   ############################*/
	
	
	private static final long serialVersionUID = 1L;
	private boolean ack ; 
	private boolean connect ;
	
	public MsgHello(String userSrc, String userDest,boolean ack,boolean connect) {
		super(userSrc, userDest);
		this.ack=ack ; 
		this.connect=connect ; 
	}
	
	public Pair<Boolean,Boolean> getContent(){
		Pair<Boolean,Boolean> p = new Pair<Boolean,Boolean>(this.ack,this.connect) ; 
		return p;
	}
	
	
}
