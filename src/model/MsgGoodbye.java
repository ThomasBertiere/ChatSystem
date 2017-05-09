package model;

public class MsgGoodbye extends Message{

	/* ############################
	  
	  Classe representant les messages
	  de deconnexion "Goodbye"

	   ############################*/
	
	private static final long serialVersionUID = 1L;

	public MsgGoodbye(String userSrc, String userDest) {
		super(userSrc, userDest);
	}
	
	
}
