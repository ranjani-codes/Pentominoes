import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Window {

	public static final int WIDTH = 600, HEIGHT = 1000;
	public static JFrame window;
	public static Grid grid;
	public static int check = 0;
	private JLabel score;
	private JLabel lCleared;

	public Window(){

		window = new JFrame("Pentis");
		score = new JLabel("Score: ");
		lCleared = new JLabel("Lines cleared: ");

		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setLayout(null);
		window.setFocusable(true);

		Font f = new Font("Monospaced", Font.PLAIN, 20);
		UIManager.put("Button.font", f);

		JButton start = new JButton("START");
		JButton reset = new JButton("RESET");
		JButton exit = new JButton("EXIT");
		JButton pause = new JButton("PAUSE/RESUME");

		score.setFont(new Font("Arial", Font.BOLD, 25));
		lCleared.setFont(new Font("Arial", Font.BOLD, 25));

		window.add(start);
		window.add(reset);
		window.add(exit);
		window.add(pause);

		window.add(score);
		window.add(lCleared);
		window.addKeyListener(grid);

		ActionListener listener = new ClickListener();
    start.addActionListener(listener);
    pause.addActionListener(listener);
		reset.addActionListener(listener);
    exit.addActionListener(listener);

		start.setBounds(345, 450, 200, 50);
		pause.setBounds(345, 500, 200, 50);
		reset.setBounds(345, 550, 200, 50);
		exit.setBounds(345, 750, 200, 50);

		score.setBounds(345, 50, 200, 100);
		lCleared.setBounds(345, 150, 200, 100);

		window.setVisible(true);

	}

	public static void start(){
		Window r = new Window();
		grid = new Grid();
		r.window.add(grid);
		r.window.addKeyListener(grid);
		r.grid.addKeyListener(grid);
		grid.setBounds(0,0, 340, 940);
	}

	public static void main(String[] args) {
		new Window();
	}
}
