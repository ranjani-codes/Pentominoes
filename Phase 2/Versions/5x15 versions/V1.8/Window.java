import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Window {

	public static final int WIDTH = 600, HEIGHT = 1000;
	public static JFrame window;
	public static Grid grid;
	public static int check = 0;
	public static JLabel score;
	public static JLabel lCleared;

	public Window(){

		window = new JFrame("Pentis");
		score = new JLabel("Score: 0");
		lCleared = new JLabel("Lines cleared: 0");

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
		JLabel nextPL = new JLabel("NEXT PENTOMINO:");

		score.setFont(new Font("Arial", Font.BOLD, 25));
		lCleared.setFont(new Font("Arial", Font.BOLD, 25));
		nextPL.setFont(new Font("Arial", Font.BOLD, 20));

		window.add(start);
		window.add(reset);
		window.add(exit);
		window.add(pause);

		window.add(score);
		window.add(lCleared);
		window.addKeyListener(grid);
		window.add(nextPL);

		ActionListener listener = new ClickListener();
    start.addActionListener(listener);
    pause.addActionListener(listener);
		reset.addActionListener(listener);
    exit.addActionListener(listener);

		start.setBounds(345, 300, 200, 50);
		pause.setBounds(345, 350, 200, 50);
		reset.setBounds(345, 400, 200, 50);
		exit.setBounds(345, 850, 200, 50);

		score.setBounds(345, 50, 200, 100);
		lCleared.setBounds(345, 150, 200, 100);
		nextPL.setBounds(350, 460, 200, 50);

		window.setVisible(true);

	}

	public static void start(){
		window.dispose();
		new Window();
		grid = new Grid();
		window.add(grid);
		window.addKeyListener(grid);
		grid.addKeyListener(grid);
		grid.setBounds(0,0, 340, 940);
	}

	public static void main(String[] args) {
		new Window();
	}
}
