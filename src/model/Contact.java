package model;

import java.net.InetAddress;

public class Contact {
	
	/* ############################
	  
	  Classe representant un contact
	  Un contact a un username, une
	  adresse ip et un boolean signalant
	  s'il est verifie ou non
	  
	   ############################*/
	
	private String username;
	private InetAddress ip;
	private Boolean checked;
	
	public Contact(String username, InetAddress ip) {
		this.username = username;
		this.ip = ip;
		this.checked=false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	
	public Boolean getChecked() {
		return checked;
	}
	
	public void setChecked(Boolean checked) {
		this.checked=checked;
	}
	
	public boolean equals(Contact other){
		return this.username.equals(other.getUsername());
	}
	
	public String affichage(){
		String res = "";
		if (this.username==null){
			res="Contact : \n     Username : Non initialisé\n     @IP : "+this.ip.toString()+"\n";
		}
		else {
			res = "Contact : \n     Username : "+this.username+"\n     @IP : "+this.ip.toString()+"\n";
		}
		return res;
	}
		
	public String toString(){
		return this.username;
	}
	
}
