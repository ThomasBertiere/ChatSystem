package gui;

import javax.swing.DefaultListModel;

import controller.Controller;

public class Vue {
	

	/* ############################
	  
	  Classe Vue du design 
	  pattern MVC. Permet de faire 
	  l'interface entre toutes les classes
	  du package Vue et le controller
	  
	   ############################*/
	
	//Les fenetre graphique sont au nombre de deux,
	//plus les popUp
	private ChatWindows chatWindows ;
	private ConnexionWindows connexionWindows;
	
	public Vue(){
		this.chatWindows = new ChatWindows();
		this.connexionWindows = new ConnexionWindows();
	}

	public ConnexionWindows getConnexionWindows() {
		return connexionWindows;
	}
	
	public ChatWindows getChatWindows() {
		return chatWindows;
	}
	
	//Fonction permettant d'ajouter un controller (un 
	//action listener) aux interfaces graphiques
	public void addControlller(Controller controller){
		this.chatWindows.addController(controller);
		this.connexionWindows.addController(controller);
	};

	//Fonction permettant d'initialiser la liste des utilisateurs connectes de la fentre de chat
	// a partir d'une liste d'username 
	public void InitListConnectedUsers(DefaultListModel<String> listConnectedUsers){
		//Creer une liste de RemoteUsers, y ajoute les utilisateurs et l'envoie a la fenetre de chat
		DefaultListModel<RemoteUsers> listRemoteUsers = new DefaultListModel<RemoteUsers>();
		int i; 
		for (i=0;i<listConnectedUsers.size();i++){
			listRemoteUsers.addElement(new RemoteUsers(listConnectedUsers.getElementAt(i),0));
		}
		this.chatWindows.InitListConnectedUsers(listRemoteUsers);
	}

	//Fonction permettant de mettre à jour (que des suppression) la liste des utilisateurs connectes 
	//de la fentre de chat a partir d'une liste d'username 
    public void updateConnectedUsers(DefaultListModel<String> listConnectedUsers){
        int i=0;
        if (listConnectedUsers.isEmpty()){
            this.chatWindows.getlistConnectedUsers().clear();
        }
        else{
            while(i<this.chatWindows.getlistConnectedUsers().size()){
                if (i>=listConnectedUsers.getSize()){
                    this.chatWindows.getlistConnectedUsers().remove(i);
                }
                else {
                    if (this.chatWindows.getlistConnectedUsers().getElementAt(i).getUsername().equals(listConnectedUsers.getElementAt(i))){}
                    else {
                        this.chatWindows.getlistConnectedUsers().remove(i);
                    }
                }
                i++;
            }
        }
    }

}
