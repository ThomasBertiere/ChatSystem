package model;

public class MsgCheck extends Message{
	
	/* ############################
	  
	  Classe representant les messages
	  "Check". Un tel message permet de
	  savoir si un utilisteur est connecte
	  (Chek_ok) suite a la reception d'un 
	  Check
	  	
	  	CHECK    -> Ack = 0 ;  
	  	CHECK_OK -> Ack = 1 ;  

	   ############################*/
	
	
	private static final long serialVersionUID = 1L;
	private boolean ack ;

	public MsgCheck(String userSrc, String userDest,boolean ack) {
		super(userSrc, userDest);
		this.ack=ack ; 
	}

	public boolean getContent(){
		return this.ack;
	}
		
}
