README du Chatsystem 

Binôme : Thomas Bertiere ; Guillaume De Brito 

Version Java : 1.8

Pour exécuter l’application il faut lancer le main présent dans la classe Main. 
Il faut lancer autant d’application que de clients désirés. Si on exécute le programme sur des machines différentes, on peut laisser le même port pour recevoir et envoyer les paquets. 
Sinon, si on utilise une seule machine, il faut changer les ports pour recevoir et envoyer (pour se faire, passer en arguments de l’appel au constructeur Network 1234,1235 pour le premier client et 1235,1234 pour le second). Attention cette manipulation restreint le nombre de client à 2 au maximum. 



Liste des fonctionnalités implémentées : 
	- Connexion 
	- Déconnexion 
	- Interface Graphique (les JScrollBar ne fonctionnent pas, si on s’envoie trop de message, on ne voit plus les derniers reçus) 
	- Envoi de message 
	- Envoi de fichier (on ne peut recevoir qu’un seul fichier, l’application ne garde pas en mémoire plusieurs fichiers)
		==> Pour envoyer et recevoir un fichier :
			- Sélectionner un utilisateur 
			- Appuyer sur « Sélectionner Fichier » puis choisir le fichier voulu dans l’explorateur de fichier, puis Ouvrir
			- Aller sur l’utilisateur distant, appuyer sur « Ouvrir Fichier Reçu » choisir l’emplacement de destination voulu, puis Sauvegarder 
	- Mécanisme de Check : l’application émet des message check auquel les autres utilisateurs doivent répondre (par un checkOk).
				Cela permet de savoir si un utilisateur a perdu la connexion de façon impromptu
				Ce mécanisme nous a paru nécessaire étant donné que nous avons choisi d’utiliser le protocole UDP dans notre application 

Attention, étant donné que l’application échange des paquets en UDP, il se peut que sur une connexion Wifi certains paquets soient perdus. C’est pourquoi nous conseillons l’utilisation d’un réseau local filaire. 

Un rapport de Test est aussi disponible sur le GitHub. 



