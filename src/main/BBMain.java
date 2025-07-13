package main;
import javax.swing.JFrame;
public class BBMain {
	public static final int WIDTH=640, HEIGHT=480;
	public static void main(String[] args) {
		JFrame theFrame = new JFrame("Brick Breaker Game");
		GamePanel thePanel = new GamePanel();
		
		theFrame.setLocation(500,200);
		theFrame.setResizable(false);
		theFrame.setSize(WIDTH, HEIGHT);
		theFrame.add(thePanel);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);
		
		thePanel.run();
	}

}
