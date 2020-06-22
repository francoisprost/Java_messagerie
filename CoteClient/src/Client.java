import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	
	private JTextField TexteUtilisateur;
	private JTextArea FenetreTchat;
	private ObjectOutputStream Sortie;
	private ObjectInputStream Entree;
	private String Pseudo = "";
	private String Message = "";
	private String IPServeur;
	private Socket Connexion;
	private JMenuBar BarreMenu;
	private JMenu BoutonOptions = new JMenu("Options");
	private JMenuItem BoutonPseudo = new JMenuItem("Modifier le pseudo");
	private JMenuItem BoutonUtilisateurs = new JMenuItem("Liste des utilisateurs");
	private JMenuItem BoutonMP = new JMenuItem("Discuter en privé");
	private JMenuItem BoutonBan = new JMenuItem("Bannir un pseudo");
	private String[] PseudosBannis = new String[] {"", "", "", ""};
	private Integer Compteur = 0;
	private JMenuItem BoutonSauvegarder = new JMenuItem("Sauvegarder la conversation");
	private JMenuItem BoutonDeconnecter = new JMenuItem("Déconnection");
	
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
		BarreMenu = new JMenuBar();
		this.BarreMenu.add(BoutonOptions);
		this.BoutonOptions.add(BoutonPseudo);
		this.BoutonOptions.add(BoutonUtilisateurs);
		this.BoutonOptions.add(BoutonMP);
		this.BoutonOptions.add(BoutonBan);
		this.BoutonOptions.add(BoutonSauvegarder);
		this.BoutonOptions.add(BoutonDeconnecter);
		//BoutonPseudo.addActionListener(new ActionListener()
		//{
		//	ChoixPseudo();
		//});
		BoutonPseudo.addActionListener( this::PseudoNewListener);
		BoutonBan.addActionListener( this::BanNewListener);
		BoutonDeconnecter.addActionListener( this::DeconnecterNewListener);
		add(BarreMenu);
		this.setJMenuBar(BarreMenu);
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
			ChoixPseudo();
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
			
		}while(!Message.equals("Serveur : Quitter"));
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
			Sortie.writeObject(Pseudo + " : " + Message);
			Sortie.flush();
			MontreMessage("\n" + Pseudo + " : " + Message);
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
	
	//Choix du pseudo
	private void ChoixPseudo() {
		  Boolean test = true;
		  JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		  String nom = jop.showInputDialog(null, "Votre pseudo", "Choix de pseudo", JOptionPane.QUESTION_MESSAGE);
		  for (int i=0; i<PseudosBannis.length; i++) {
			  System.out.print("\n i" + PseudosBannis[i].toString() + "i"); //Test egalité
			  System.out.print("\n i" + nom + "i");
			  if (PseudosBannis[i].toString().equals(nom) == true) {
				  test = false;
			  }
		  }
		  if (test == true) {
			  jop2.showMessageDialog(null, "Votre pseudo est " + nom, "Choix de pseudo", JOptionPane.INFORMATION_MESSAGE);
			  Pseudo = nom;
		  }
		  if (test == false) {
			  jop2.showMessageDialog(null, "Le pseudo " + nom + " est banni.", "Choix de pseudo", JOptionPane.INFORMATION_MESSAGE);
		  }
	}
	
	//Mise à jour du pseudo
    public void PseudoNewListener( ActionEvent event ) {
        ChoixPseudo();
    }
	
    
    //Déconnection
    private void DeconnecterNewListener( ActionEvent event ) {
    	this.dispose();
    }
    
  //Bannir un pseudo
    private void BanNewListener( ActionEvent event ) {
    	JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		  String nom = jop.showInputDialog(null, "Pseudo à bannir", "Bannissement de pseudo", JOptionPane.QUESTION_MESSAGE);
		  jop2.showMessageDialog(null, "Le pseudo banni est " + nom, "Bannissement de pseudo", JOptionPane.INFORMATION_MESSAGE);
		  PseudosBannis[Compteur] = nom;
		  Compteur += Compteur;
		  if (Compteur == PseudosBannis.length) {
			  Compteur = 0;
		  }
    }
}
