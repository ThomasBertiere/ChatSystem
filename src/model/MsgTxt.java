package model;

public class MsgTxt extends Message{

	/* ############################
	  
	  Classe representant les messages
	  transportant un texte.

	   ############################*/
	
	private static final long serialVersionUID = 1L;
	String data ;

	public MsgTxt(String userSrc, String userDest, String data) {
		super(userSrc, userDest);
		this.data = data;
	} 
	
	public String getContent(){
		return this.data;
	}
	
}
