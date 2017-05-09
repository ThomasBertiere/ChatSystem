package test;

import controller.Controller;
import gui.Vue;
import model.Model;
import network.Network;

/**
 * Cette classe sert uniquement a ne pas afficher les pop-up pour rendre les test vraiment automatique
 * On ne fait que hériter le controller et on redéfinit les méthodes qui doivent normalement lancer les pop-up 
 * Ici ces méthodes ne font rien 
 * 
 * A n'utiliser que dans le cadre des test JUnit
 * N'est accessible que dans le package test
 * 
 * @author Guillaume
 *
 */
class ControllerSansPopUp extends Controller {
	public ControllerSansPopUp(Model model, Network network, Vue vue) {
		super(model, network, vue);
	}
	protected void deconnexionPopUp() {
	}
	protected void popUpConnexionFailed() {
	}
}