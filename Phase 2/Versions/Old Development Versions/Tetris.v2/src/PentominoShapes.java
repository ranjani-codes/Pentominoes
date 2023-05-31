import java.awt.*;

public class PentominoShapes {

    private int [][] position;//coordinates of a pentomino
    private int [] color;//Color of the pentomino
    private Grid grid; //make an object grid to access the class Grid
    private String keyPressed = ""; //Stores the key that is pressed
    private int deltaX = 0;
    private int xPos = 3; //Middle of the grid of 10
    private int yPos = 0; //On the top of the grid


    public PentominoShapes(int [][] position, int [] color, Grid grid){
        this.position = position;
        this.color = color;
        this.grid = grid;
    }

    public void Update(){
        xPos += deltaX;

        deltaX = 0;
    }

    public void Render(Graphics g){

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (position[x][y] != 0) {
                    g.setColor(new Color(color[0], color[1], color[2]));
                    g.fillRect((grid.getMarge() + x*grid.getSize1Block() +xPos*grid.getSize1Block()), (grid.getMarge() + y*grid.getSize1Block() + yPos*grid.getSize1Block()), grid.getSize1Block() , grid.getSize1Block());
                }
            }
        }
    }

    public void setDeltaX(int deltaX){
        this.deltaX = deltaX;
    }


}
