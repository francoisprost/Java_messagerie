import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField TexteUtilisateur;
	private JTextArea FenetreTchat;
	private ObjectOutputStream Sortie;
	private ObjectInputStream Entree;
	private String Message = "";
	private String IPServeur;
	private Socket Connexion;
	
	//Constructeur
	public Client(String hote){
		super("Messagerie instantanée (Client)");
		IPServeur = hote;
		TexteUtilisateur = new JTextField();
		TexteUtilisateur.setEditable(false);
		TexteUtilisateur.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						EnvoieMessage(event.getActionCommand());
						TexteUtilisateur.setText("");
					}
				}
		);
		add(TexteUtilisateur, BorderLayout.SOUTH);
		FenetreTchat = new JTextArea();
		FenetreTchat.setEditable(false);
		add(new JScrollPane(FenetreTchat), BorderLayout.CENTER);
		setSize(600,300);
		setVisible(true);
	}
	
	//Connexion au serveur
	public void startRunning() {
		try {
			ConnexionServeur();
			InitialisationFlux();
			PendantConversation();
		}
		catch(EOFException eofException) {
			MontreMessage("\n Le client a arrêté la connexion");
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
		finally {
			FermerFlux();
		}
	}
	
	//Connexion au serveur
	private void ConnexionServeur() throws IOException{
		MontreMessage("Tentative de connexion... \n");
		Connexion = new Socket(InetAddress.getByName(IPServeur), 6789);
		MontreMessage("Connecté à: " + Connexion.getInetAddress().getHostName());
	}
	
	//Initialiser le flux pour recevoir et envoyer des messages
	private void InitialisationFlux() throws IOException{
		Sortie = new ObjectOutputStream(Connexion.getOutputStream());
		Sortie.flush();
		Entree = new ObjectInputStream(Connexion.getInputStream());
		MontreMessage("\n Vous êtes maintenant connecté \n");
	}
	
	//Pendant la conversation avec le serveur
	private void PendantConversation() throws IOException{
		DroitEcriture(true);
		do {
			try {
				Message = (String) Entree.readObject();
				MontreMessage("\n" + Message);
			}
			catch (ClassNotFoundException classNotfoundException){
				MontreMessage("\n Message non recevable");
			}
			
		}while(!Message.equals("SERVEUR - END"));
	}
	
	//Ferme les flux et prises après avoir fini de discuter
	private void FermerFlux(){
		MontreMessage("\n Connexion en cours de fermeture...");
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
	
	//Envoyer des messages au serveur
	private void EnvoieMessage(String Message) {
		try {
			Sortie.writeObject("CLIENT - " + Message);
			Sortie.flush();
			MontreMessage("\nCLIENT - " + Message);
		}
		catch(IOException ioException) {
			FenetreTchat.append("\n Erreur: Le message n'arrive pas à être envoyé");
		}
	}
	
	//Actualise la FenetreTchat
	private void MontreMessage(final String Message){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						FenetreTchat.append(Message);
					}
				}
		);
	}
	
	//Donne à l'utilisateur le droite d'écrire dans la boite de dialogue
	private void DroitEcriture(final boolean droit){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						TexteUtilisateur.setEditable(droit);
					}
				}
		);
	}
	
	
}
