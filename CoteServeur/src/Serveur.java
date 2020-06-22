import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Serveur extends JFrame{
	
	private JTextField TexteUtilisateur;
	private JTextArea FenetreTchat;
	private ObjectOutputStream Sortie;
	private ObjectInputStream Entree;
	private ServerSocket Serveur;
	private Socket Connexion;
	
	// Constructeur
	public Serveur() { 
		super("Messagerie instantanée (Serveur)");
		TexteUtilisateur = new JTextField();
		TexteUtilisateur.setEditable(false);
		TexteUtilisateur.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						EnvoieMessage(event.getActionCommand());
						TexteUtilisateur.setText("");
					}
				
				}
		);
		add(TexteUtilisateur, BorderLayout.SOUTH);
		FenetreTchat = new JTextArea();
		FenetreTchat.setEditable(false);
		add(new JScrollPane(FenetreTchat));
		setSize(600,300);
		setVisible(true);
	}
	
	//Mise en place et fonctionnement du serveur
	public void startRunning() {
		try {
			Serveur = new ServerSocket(6789, 100);
			while(true) {
				try {
					AttendreConnexion();
					InitialisationFlux();
					PendantConversation();
				}
				catch(EOFException eofException) {
					MontreMessage("\n Le serveur a arrêté la connection");
				}
				finally {
					FermerFlux();
				}
			}
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//Attendre la connexion, puis afficher les informations de connexion
	private void AttendreConnexion() throws IOException{
		MontreMessage("En attente de la connexion d'un autre utilisateur... \n");
		Connexion = Serveur.accept();
		MontreMessage("Maintenant connecté à " + Connexion.getInetAddress().getHostName());
	}
	
	//Permet au flux d'envoyer et de recevoir de l'information
	private void InitialisationFlux() throws IOException{
		Sortie = new ObjectOutputStream(Connexion.getOutputStream());
		Sortie.flush();
		Entree = new ObjectInputStream(Connexion.getInputStream());
		MontreMessage("\n Le flux est maintenant initialisé \n");
	}
	
	//Pendant la conversation tchat
	private void PendantConversation() throws IOException{
		String Message = " Vous êtes maintenant connecté ";
		EnvoieMessage(Message);
		DroitEcriture(true);
		do {
			try {
				Message = (String) Entree.readObject();
				MontreMessage("\n" + Message);
			}
			catch(ClassNotFoundException classNotFoundException) {
				MontreMessage("\n Message non recevable");
			}
		}
		while(!Message.equals("END"));
	}
	
	//Ferme les flux et prises après avoir fini de discuter
	private void FermerFlux() {
		MontreMessage("\n Connexion en cours de fermeture... \n");
		DroitEcriture(false);
		try {
			Sortie.close();
			Entree.close();
			Connexion.close();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//Envoie un message au client
	private void EnvoieMessage(String Message) {
		try {
			Sortie.writeObject("Serveur : " + Message);
			Sortie.flush();
			MontreMessage("\nServeur : " + Message);
		}
		catch(IOException ioException) {
			FenetreTchat.append("\n Erreur: Le message n'arrive pas à être envoyé");
		}
	}
	
	//Actualise la FenetreTchat
	private void MontreMessage(final String texte){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run() {
						FenetreTchat.append(texte);
					}				
				}
			);
	}
	
	//Laisse l'utilisateur écrire dans la fenêtre de discussion
	private void DroitEcriture(final boolean droit){
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						TexteUtilisateur.setEditable(droit);
					}
				}
		);
	}
	

}
