import javax.swing.JFrame;

public class TestServeur {
	
	public static void main(String[] args) {
		Serveur Test = new Serveur();
		Test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Test.startRunning();
	}

}
