package model;

import java.io.Serializable;

public abstract class Message implements Serializable{
	
	/* ############################
	  
	  Classe abstraite permettant de 
	  representer un message qui sera
	  echanger entre des utilisateurs.
	  Elle implemente serializable pour 
	  qu'on objet message puisse etre 
	  convertit en tableau d'octet et 
	  envoye a un destinataire.
	  En general un message contient 
	  un username source et distant. 
	  Le contenu varie selon le type
	  
	   ############################*/
	
	private static final long serialVersionUID = 1L;
	protected String userSrc ; 
	protected String userDest ;
	
	public Message(String userSrc, String userDest) {
		this.userSrc = userSrc;
		this.userDest = userDest;
	} 
	
	public String getUserSrc() {
		return userSrc;
	}

	public void setUserSrc(String userSrc) {
		this.userSrc = userSrc;
	}

	public String getUserDest() {
		return userDest;
	}

	public void setUserDest(String userDest) {
		this.userDest = userDest;
	}

	public MessageType getType() {
		if(this instanceof MsgHello) {
			return MessageType.HELLO ; 
		} 
		else if (this instanceof MsgCheck) {
			return MessageType.CHECK ; 
		}
		else if (this instanceof MsgGoodbye) {
			return MessageType.GOODBYE ; 
		}
		else if (this instanceof MsgTxt) {
			return MessageType.TXT ; 
		}
		else if(this instanceof MsgFile) {
			return MessageType.FILE ; 
		}
		else {
			return MessageType.EXT; 
		}
	}
}
