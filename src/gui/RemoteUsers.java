package gui;

public class RemoteUsers {
	

	/* ############################
	  
	  Classe representant un utilisateur
	  distant et son nombre de message recu
	  Permet a la fentre de chat d'avoir une
	  liste d'utilisateurs connectes avec le
	  nombre de message non lu
	  
	   ############################*/
	
	private String username ;
	private int nbMessageReceived;
	
	public RemoteUsers(String username, int nbMessageReceived){
		this.setUsername(username);
		this.setNbMessageReceived(nbMessageReceived);
	}

	public int getNbMessageReceived() {
		return nbMessageReceived;
	}

	public void setNbMessageReceived(int nbMessageReceived) {
		this.nbMessageReceived = nbMessageReceived;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	//fonction qui sera utilisee pour l'affichage d'un RemoteUsers
	//dans un Jlist
	public String toString(){
		String res="";
		if (this.nbMessageReceived==0){
			res=this.username;
		}
		else {
			res=this.username+" ("+this.nbMessageReceived+")";
		}
		return res;
	}

}
