import javax.swing.JFrame;

public class TestClient2 {

	public static void main(String[] args) {
		Client Test;
		Test = new Client("127.0.0.2"); //Pour tester, 127.0.0.1 est l'hote local (nous)
		Test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Test.startRunning();

	}

}
