import javax.swing.JFrame;

public class Window {
	
	public static final int WIDTH = 310, HEIGHT = 645;
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
