package model;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.DefaultListModel;


public class Model {


	/* ############################
	  
	  Classe Model du design 
	  pattern MVC. Permet de faire 
	  l'interface entre toutes les classes
	  du package Model et le controller
	  
	   ############################*/
	
	//un modele a une liste d'utilisateurs connectes et son 
	//propre contact
	private ConnectedUsers users;
	
	public Model(){}
	
	public void createListConnectedUser(){
		users = ConnectedUsers.getInstance();
	}

	public Contact createContact(String username, InetAddress ip){
		return new Contact(username,ip);
	}
	
	public void setMySlef(Contact mySelf){
		this.users.setMyself(mySelf.getUsername(),mySelf.getIp());
	}
	
	public Contact getMySlef(){
		return this.users.getMyself();
	}
	
	public void removeMySelf(){
		this.users.setMyself("",null);
	}
	
	public DefaultListModel<Contact> getListConnectedUsers(){
		return this.users.getListConnectedUsers();
	}
	
	public void addListConnectedUser(Contact remoteContact) {
		this.users.addConnectedUser(remoteContact);
	}
	
	public void removeConnectedUser(String username){
		try {
			this.users.removeConnectedUser(username);
		} catch (IOException e) {
			//si l'exception est levee, ie qu'il n'y a pas 
			//d'utilisateur correspondant, on ne fait rien
		}
	}
	
	public Contact getConnectedUser(String username) throws IOException{
		return this.users.getConnectedUser(username);
	}
	
	public boolean checkUsernameNotExists(String username){
		 return !this.users.userAlreadyExists(username);
	}
	
	public void uncheckAllUsers() {
		this.users.uncheckAllUsers();
	}
	
	public void removeUncheckedUsers() {
		this.users.removeNoneCheckUsers();
	}
	
	public void removeListConnectedUsers() {
		this.users.removeAllConnectedUser();
	}

	public DefaultListModel<String> convertListToString(){
		return this.users.convertListConnectedUsersToString();
	}
	
	public String listToString(){
		return this.users.listToString();
	}
	
	/**
	 * Parameters first and second are optional
	 * @param userSrc
	 * @param userDest
	 * @param type
	 * @param first
	 * @param second
	 * @return
	 */
	public Message createMessage(String userSrc, String userDest,MessageType type,boolean first,boolean second){
		
		switch(type) {
			case HELLO : 
				return new MsgHello(userSrc,userDest,first,second);				
			case CHECK : 
				return new MsgCheck(userSrc,userDest,first);
			default : 
				return new MsgGoodbye(userSrc,userDest);
		}
	}
	
	public Message createMessageTXT(String userSrc,String userDest,String data) {
		return new MsgTxt(userSrc,userDest,data);
	}
	
	public Message createMessageFile(String userSrc,String userDest,File file) {
		return new MsgFile(userSrc,userDest,file) ; 
	}
	
	public Message createMessageExt(String userSrc,String userDest,byte[] octets) {
		return new MsgExt(userSrc,userDest,octets) ;
	}
	
	
}
