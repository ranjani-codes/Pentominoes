import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static java.util.Arrays.*;

public class Grid extends JPanel implements KeyListener {
    private final int size1Block = 40; //size by pixel per block
    private final int xAxis = 10; //number of x boxes
    private final int yAxis = 20; //number of y boxes
    private int numberOfPentominos = 12; //number of shapes
    private int marge = 10; //pixels from the border before the grid

    private int width_grid = size1Block*xAxis;  //total width grid dimensions
    private int height_grid = size1Block*yAxis; //total height grid dimensions
    private int [][] grid = new int[yAxis][xAxis]; //array of the grid

    private PentominoShapes[]  pentominoShapes = new PentominoShapes[numberOfPentominos]; //12 pentomino's in total
    private PentominoShapes shapeN; //shapeN of the Pentomino we're dealing with
    private int nextShapeN;

    private Timer timer; //deals with all the timer stuff
    private final int fps = 60; //refresh screen per second
    private int delay = (int)(1000/fps); //delay/time between each refreshment

    private boolean gameOver = false;


    public Grid(){

        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Update(); //update will call the Update in the PentominoShape class
                repaint(); //repaint fps(60)/sec
            }
        });

        timer.start();

        //Shapes:
        //L
        pentominoShapes[0] = new PentominoShapes(new int[][]{{0,0,0,1},{1,1,1,1}}, 1,this);
        //P
        pentominoShapes[1] = new PentominoShapes(new int[][]{{1,1},{1,1},{1,0}}, 2,this);
        //X
        pentominoShapes[2] = new PentominoShapes(new int[][]{{0,1,0},{1,1,1},{0,1,0}}, 3,this);
        //F
        pentominoShapes[3] = new PentominoShapes(new int[][]{{0,1,1},{1,1,0},{0,1,0}}, 4,this);
        //V
        pentominoShapes[4] = new PentominoShapes(new int[][]{{1,0,0},{1,0,0},{1,1,1}}, 5,this);
        //W
        pentominoShapes[5] = new PentominoShapes(new int[][]{{1,0,0},{1,1,0},{0,1,1}}, 6,this);
        //Y
        pentominoShapes[6] = new PentominoShapes(new int[][]{{0,1},{1,1},{0,1},{0,1}}, 7,this);
        //I
        pentominoShapes[7] = new PentominoShapes(new int[][]{{1,1,1,1,1}}, 8,this);
        //T
        pentominoShapes[8] = new PentominoShapes(new int[][]{{1,1,1},{0,1,0},{0,1,0}}, 9,this);
        //Z
        pentominoShapes[9] = new PentominoShapes(new int[][]{{1,1,0},{0,1,0},{0,1,1}}, 10,this);
        //U
        pentominoShapes[10] = new PentominoShapes(new int[][]{{1,0,1},{1,1,1}}, 11,this);
        //N
        pentominoShapes[11] = new PentominoShapes(new int[][]{{1,1,0,0},{0,1,1,1}},12,this);

        nextPentomino();

    }

    //generates a new pentomino number
    public void nextPentomino(){
        nextShapeN = new RandomGenerator(numberOfPentominos).Generator(); //going in the RandomGenerator class to pick a new number
        System.out.println("GENERATED  " + nextShapeN);
        shapeN = pentominoShapes[6]; //assign a number to the pentomino


        int [][] position = pentominoShapes[nextShapeN].getPosition();
        for (int y = 0; y < position.length; y++){
            for (int x = 0; x < position[0].length; x++){
                if (position[y][x] != 0){ //only look at the 1's in the matrix
                    if (grid[y][x + 2] != 0){//+2 because it's just before the top of grid, play with this parameter to see the difference
                        gameOver = true; //meaning we reached the top and it will stop the game
                    }
                }
            }
        }

    }

    public void Update(){

        shapeN.Update(); //take the shapeN and go to the PentominoShapes class with it
        if (gameOver){
            timer.stop(); //stop generating pieces
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        shapeN.Render(g); //take the shapeN and render it in the PentominoShapes class

        //Check if there is a filled block in the grid, if yes show the pentomino on the grid with it's respective color
        for(int y = 0; y< grid.length; y++){
            for (int x = 0; x < grid[x].length; x++){
                if (grid[y][x] != 0){ //if the block is not empty
                    int [] colorArray = new ColorPentomino(grid[y][x]-1).GiveColor(); //take the color array of the pentomino
                    g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
                    g.fillRect(marge + x*size1Block,marge + y*size1Block, size1Block, size1Block); //fill the rectangle
                }
            }
        }

        //Draw the grid
        for (int x = marge; x <= width_grid; x += size1Block) {
            for (int y = marge ; y <= height_grid; y += size1Block) {
                g.setColor(Color.black); //lines of the grid are black
                g.drawRect(x, y, size1Block, size1Block); //will draw the rectangle without filling it. only the borders of it will be drawn
            }
        }
    }

    public int [][] getGrid()  {return grid;};

    @Override
    //Keyevents when a key is pressed
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                shapeN.setDeltaX(-1); //1 block to the left, - direction
                break;
            case KeyEvent.VK_RIGHT:
                shapeN.setDeltaX(1); //1 block to the right, + direction
                break;
            case KeyEvent.VK_DOWN:
                shapeN.setSpeedDown(); //will accelerate going down
                break;
            case KeyEvent.VK_UP:
                shapeN.Rotate(); //rotate the pentomino
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) { //we need to stop the block to go infinitely  down
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            shapeN.setNormalSpeed(); //reset the speed to the normal speed
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} //not needed, but need to be written. it's part of the "implements KeyListener"


    public int getSize1Block(){return size1Block;}
    public int getMarge() {return marge;}
    public int getxAxis() {return xAxis;}
    public int getyAxis() {return yAxis;}

}
