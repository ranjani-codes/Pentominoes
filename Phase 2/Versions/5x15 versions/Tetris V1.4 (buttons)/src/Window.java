import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Window {

	public static final int WIDTH = 500, HEIGHT = 1000;
	public static JFrame window;
	public static Grid grid;
	public static int check = 0;

	public Window(){
		window = new JFrame("Pentis");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setLayout(null);

		
		Font f = new Font("Monospaced", Font.PLAIN, 20);
		UIManager.put("Button.font", f);

		JButton start = new JButton("START");
		JButton reset = new JButton("RESET");
		JButton exit = new JButton("EXIT");
		JButton pause = new JButton("PAUSE/RESUME");


		window.add(start);
		window.add(reset);
		window.add(exit);
		window.add(pause);

		ActionListener listener = new ClickListener();
    	start.addActionListener(listener);
	    pause.addActionListener(listener);
		reset.addActionListener(listener);
    	exit.addActionListener(listener);

		start.setBounds(345, 200, 120, 100);
		start.setBackground(Color.yellow);

		pause.setBounds(345, 330, 120, 100);
		pause.setBackground(Color.orange);

		reset.setBounds(345, 460, 120, 100);
		reset.setBackground(Color.GREEN);
		
		exit.setBounds(345, 850, 120, 100);
		exit.setBackground(Color.red);

		window.setVisible(true);
	}

	public static void main(String[] args) {
		new Window();
	}
}
