package model;

public class MsgExt extends Message{

	
	/* ############################
	  
	  Classe representant les messages
	  etendu. Un tel message permet d'
	  envoyer un tableau d'octet. Il 
	  permet donc d'envoyer n'importe
	  quelle donnee dont nous avons pas 
	  tenu compte dans les autres types 
	  de messages

	   ############################*/

	private static final long serialVersionUID = 1L;
	private byte[] tabOctet;

	public MsgExt(String userSrc, String userDest,byte[] tabOctet) {
		super(userSrc, userDest);
		this.tabOctet=tabOctet ; 
	}

	public byte[] getContent(){
		return this.tabOctet;
	}
	
	
}
