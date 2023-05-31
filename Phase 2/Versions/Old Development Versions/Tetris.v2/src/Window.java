import javax.swing.JFrame;
import java.awt.*;

public class Window {

    public static final int width_pixels = 420, heigth_pixels = 855; //Size1block*Axis +20 and +55
    private JFrame window;
    private Grid board;

    public Window(){
        window = new JFrame("Pentis");
        window.setSize(width_pixels, heigth_pixels);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setBackground(Color.white);

        board = new Grid();
        window.add(board);

        window.addKeyListener(board);

        window.setVisible(true);

    }


    public static void main(String[] args) {new Window();}
}