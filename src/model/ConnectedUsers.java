package model;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.DefaultListModel;

public class ConnectedUsers {
	
	/* ############################
	  
	  Classe permettant d'avoir et 
	  de manipuler une liste d'
	  utilisateurs connectes.
	  Il existe une seule liste de
	  connectedUsers, on utilise donc
	   le design pattern singleton
	  
	   ############################*/
	
	//Instance de la classe selon le design pattern singleton
	private static ConnectedUsers instance = new ConnectedUsers();
	//liste de contacts connectes
	private DefaultListModel<Contact> listConnectedUsers ;
	//notre propre contact
	private Contact mySelf; 
	
	private ConnectedUsers(){
		this.createListConnectedUsers();
		this.mySelf = new Contact(null,null);
	}
	
	public static ConnectedUsers getInstance(){
		return instance;
	}
	
	public Contact getMyself(){
		return this.mySelf;
	}
	
	public void setMyself(String username,InetAddress ip){
		this.mySelf.setUsername(username);
		this.mySelf.setIp(ip);
	}
	
	public void createListConnectedUsers(){
		this.listConnectedUsers=new DefaultListModel<Contact>();
	}
	
	public DefaultListModel<Contact> getListConnectedUsers(){
		return this.listConnectedUsers;
	}
	
	//Ajoute un contact a notre liste selon l'ordre alphabetique
	public void addConnectedUser(Contact user){
		if (this.listConnectedUsers.isEmpty()){
			this.listConnectedUsers.addElement(user);	
		}
		else {
			int i=0;
			boolean notAdd = true ;
			while (i<this.listConnectedUsers.getSize() & notAdd){
				if (user.getUsername().compareTo(this.listConnectedUsers.getElementAt(i).getUsername())<=0){
					notAdd=false;
					this.listConnectedUsers.add(i,user);
				}
				i++;
			}
			if(i==this.listConnectedUsers.getSize()){
				this.listConnectedUsers.addElement(user);	
			}
		}
	} 
	
	//Retourne un contact connecte present dans la liste à partir de son 
	//username. Si celui ci n'existe pas une exception est renvoyee
	public Contact getConnectedUser(String username) throws IOException{
		int i ;
		Contact res = null;
		for (i=0;i<this.listConnectedUsers.getSize();i++){
			if (this.listConnectedUsers.getElementAt(i).getUsername().equals(username)){
				res=this.listConnectedUsers.getElementAt(i);
			}
		}
		if (res == null){
			throw new IOException("Element non present dans la liste des utilisateurs connectes");
		}
		return res;		
	}
	
	//Supprime un contact connecte present dans la liste à partir de son 
	//username. Si celui ci n'existe pas une exception est renvoyee
	public void removeConnectedUser(String username) throws IOException{
		int i ;
		int res = -1; 
		for (i=0;i<this.listConnectedUsers.getSize();i++){
			if (this.listConnectedUsers.getElementAt(i).getUsername().equals(username)){
				res = i ;
				break;
			}
		}
		if (res==-1){
			throw new IOException("Element non present dans la liste des utilisateurs connectes");
		}
		else {
			this.listConnectedUsers.remove(res);
		}
	}
	
	public void removeNoneCheckUsers(){
		int i;
		for (i=0;i<this.listConnectedUsers.getSize();i++){
			if (this.listConnectedUsers.getElementAt(i).getChecked()==false){
				this.listConnectedUsers.remove(i);
			}
		}
	}
	
	//retourne vrai si un contact connecte a l'username specifie
	public Boolean userAlreadyExists(String username){
		int i=0 ;
		boolean res = false;
		while (i<this.listConnectedUsers.getSize() && !res){
			if (this.listConnectedUsers.getElementAt(i).getUsername().equals(username)){
				res=true;
			}
			i++;
		}
		if (res==false){
			if (this.mySelf.getUsername().equals(username)){
				res=true;
			}
		}
		return res;
	}
	
	public void uncheckAllUsers() {
		for(int i =0; i<this.listConnectedUsers.size() ; i++) {
			this.listConnectedUsers.getElementAt(i).setChecked(false);
		}
	}
	
	public void removeAllConnectedUser(){
		this.listConnectedUsers.clear();
	}

	public String listToString(){
		String res="ListConnectedUsers : \n";
		if (this.listConnectedUsers.isEmpty()){
			res=res+"vide\n";
		}
		else {
			int i;
			for (i=0;i<this.listConnectedUsers.getSize();i++){
				res=res+"     ####################\n     "+this.listConnectedUsers.getElementAt(i).toString();
			}
		}
		return res;
	}
	
	//Convertit la liste de Contact en liste de string. Les strings etant
	//les usernames des contacts
	public DefaultListModel<String> convertListConnectedUsersToString(){
		DefaultListModel<String> res= new DefaultListModel<String>();
		int i;
		for(i=0;i<this.listConnectedUsers.size();i++){
			res.addElement(this.listConnectedUsers.getElementAt(i).getUsername());
		}
		return res;
	}
	
	
}
