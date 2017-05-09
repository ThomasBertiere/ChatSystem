package model;

import java.io.File;

public class MsgFile extends Message{

	/* ############################
	  
	  Classe representant les messages
	  transportant un fichier.

	   ############################*/

	private static final long serialVersionUID = 1L;
	private File file ;

	public MsgFile(String userSrc, String userDest,File file){
		super(userSrc, userDest);
		this.file=file;
	}

	public File getContent(){
		return this.file;
	}
	
}
