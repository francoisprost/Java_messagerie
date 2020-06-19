import javax.swing.JFrame;

public class TestClient {
	
	public static void main(String[] args) {
		Client Test2;
		Test2 = new Client("127.0.0.1"); //Pour tester, 127.0.0.1 est l'hote local (nous)
		Test2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Test2.startRunning();
	}

}
