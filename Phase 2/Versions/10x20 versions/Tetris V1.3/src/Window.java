import javax.swing.JFrame;
import java.awt.*;

public class Window {
	
	public static final int WIDTH = 420, HEIGHT = 855;
	private JFrame window;
	private Grid grid;
	
	public Window(){
		window = new JFrame("Pentis");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);

		grid = new Grid();

		window.add(grid);
		window.addKeyListener(grid);

		window.setVisible(true);
	}

	public static void main(String[] args) {
		new Window();
	}
}
