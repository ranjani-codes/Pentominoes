import javax.swing.*;
import java.awt.*;


public class Grid extends JPanel {
    private final int size1Block = 40; //Size by pixel per block
    private final int xAxis = 10; //Number of x boxes
    private final int yAxis = 20; //Number of y boxes
    private int width_grid = size1Block*xAxis, height_grid = size1Block*yAxis; //Total grid dimensions
    private int [][] grid = new int[xAxis][yAxis]; //Array of the grid

    public Grid(){

    }

    @Override
    public void paint(Graphics g) {
        for (int x = 10; x <= width_grid; x += size1Block) { //10 is for the marge before the grid
            for (int y = 10 ; y <= height_grid; y += size1Block) { //10 is for the marge before the grid
                g.setColor(Color.black);
                g.drawRect(x, y, size1Block, size1Block);
            }
        }
    }
}
