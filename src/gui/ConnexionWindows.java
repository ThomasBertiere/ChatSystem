package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

import controller.Controller;

public class ConnexionWindows extends JFrame {
	

	/* ############################
	  
	  Classe ConnexionWindows definissant 
	  la fenetre de connexion
	  
	   ############################*/

	private JButton bConnexion;
	private JTextArea tUsername;
	private JLabel lConnexion ; 
	private JLabel lUsername ; 
	
	public ConnexionWindows(){
		this.init();
	}
	
	//L'action listener de cette fenetre deviendra le controller
	public void addController(Controller controller){
		bConnexion.addActionListener(controller);
	}

	public JButton getbConnexion(){
		return this.bConnexion;
	}
	
	public String getUsername(){
		return this.tUsername.getText();
	}
	
	private void init(){

		this.setSize(300, 145);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		bConnexion=new JButton("Connexion");
		bConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
		tUsername=new JTextArea();
		tUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		tUsername.setMaximumSize(new Dimension(250, 19));
		tUsername.setMinimumSize(new Dimension(250, 19));
		lConnexion=new JLabel("Bienvenue sur notre ChatSystem");
		lConnexion.setFont(new Font("",1,15));
		lConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
		lUsername=new JLabel("Identifiant :");
		lUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

		
		JPanel b1 = new JPanel();
	    b1.setLayout(new BoxLayout(b1, BoxLayout.PAGE_AXIS));
   	    b1.add(lConnexion);
	    b1.add(Box.createRigidArea(new Dimension(0, 20)));
	    b1.add(lUsername);
	    b1.add(Box.createRigidArea(new Dimension(0, 10)));
	    b1.add(tUsername);
	    b1.add(Box.createRigidArea(new Dimension(0, 10)));
	    b1.add(bConnexion);

	    
	    this.getContentPane().add(b1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//invisible par defaut
		this.setVisible(false);
	}

	
	public void displayConnexionWindows(){
		this.setVisible(true);
	}
	
	public void closeConnexionWindows(){
		this.dispose();
	}
	
	//PopUp d'erreur lorsque la connexion a echoue
	public void launchConnexionFailedPopUp(){
		JOptionPane.showMessageDialog(this, "Connexion impossible. L'identifiant n'est pas correct","Erreur de connexion",JOptionPane.ERROR_MESSAGE);
	}
	
}
