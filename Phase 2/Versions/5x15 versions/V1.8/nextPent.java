import java.util.*;
import java.lang.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class nextPent extends JPanel{

  PentominoShape nextShape;

  public nextPent(){
    nextShape = Grid.pentominoShapes[(int)Math.random()*Grid.numberOfPentominos];
  }

  public void paintComponent(Graphics g) {


		for (int y = 0; y < nextShape.getPos().length; y++) {
			for (int x = 0; x < nextShape.getPos()[y].length; x++) {
				if (nextShape.getPos()[y][x] != 0) {
					int[] colorArray = new ColorPentomino(nextShape.getPos()[y][x]-1).GiveColor();
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
					g.fillRect(x * 60, y * 60, 60, 60); //fill the rectangle
				}
			}
		}
  }

}
