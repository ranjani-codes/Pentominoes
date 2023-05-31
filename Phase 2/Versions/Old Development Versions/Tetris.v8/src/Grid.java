import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Grid extends JPanel implements KeyListener {
    private final int size1Block = 40; //size by pixel per block
    private final int xAxis = 10; //number of x boxes
    private final int yAxis = 20; //number of y boxes

    private int width_grid = size1Block*xAxis;  //total width grid dimensions
    private int height_grid = size1Block*yAxis; //total height grid dimensions
    private int marge = 10; //pixels from the border before the grid
    private int [][] grid = new int[xAxis][yAxis]; //array of the grid

    private int numberOfShapes = 12;
    private PentominoShapes[]  pentominoShapes = new PentominoShapes[numberOfShapes]; //12 shapes in total
    private PentominoShapes currentShape; //shape of the Pentomino we're dealing with

    private Timer timer;
    private final int fps = 60; //refresh screen per second
    private int delay = (int)(1000/fps); //delay/time between each refreshment


    public Grid(){

        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Update(); //update will call the Update in the PentoninoShape class
                repaint(); //repaint fps(60)/sec
            }
        });

        timer.start();

        //Shapes:
        //L
        pentominoShapes[0] = new PentominoShapes(new int[][]{{0,0,0,1},{1,1,1,1}}, new int []{25,190,155},this);
        //P
        pentominoShapes[1] = new PentominoShapes(new int[][]{{1,1},{1,1},{1,0}}, new int []{45,205,115},this);
        //X
        pentominoShapes[2] = new PentominoShapes(new int[][]{{0,1,0},{1,1,1},{0,1,0}}, new int []{50,150,220},this);
        //F
        pentominoShapes[3] = new PentominoShapes(new int[][]{{0,1,1},{1,1,0},{0,1,0}}, new int []{95,90,180},this);
        //V
        pentominoShapes[4] = new PentominoShapes(new int[][]{{1,0,0},{1,0,0},{1,1,1}}, new int []{50,75,95},this);
        //W
        pentominoShapes[5] = new PentominoShapes(new int[][]{{1,0,0},{1,1,0},{0,1,1}}, new int []{240,195,15},this);
        //Y
        pentominoShapes[6] = new PentominoShapes(new int[][]{{0,1},{1,1},{0,1},{0,1}}, new int []{230,125,35},this);
        //I
        pentominoShapes[7] = new PentominoShapes(new int[][]{{1,1,1,1,1}}, new int []{230,75,60},this);
        //T
        pentominoShapes[8] = new PentominoShapes(new int[][]{{1,1,1},{0,1,0},{0,1,0}}, new int []{150,165,165},this);
        //Z
        pentominoShapes[9] = new PentominoShapes(new int[][]{{1,1,0},{0,1,0},{0,1,1}}, new int []{245,155,20},this);
        //U
        pentominoShapes[10] = new PentominoShapes(new int[][]{{1,0,1},{1,1,1}}, new int []{210,85,0},this);
        //N
        pentominoShapes[11] = new PentominoShapes(new int[][]{{1,1,0,0},{0,1,1,1}},new int []{190,57,43},this);

        nextPiece();
    }

    public void nextPiece(){
        int nextShapeNumber;
        nextShapeNumber = new RandomGenerator(numberOfShapes).Generator();
        currentShape = pentominoShapes[nextShapeNumber];
    }

    public void Update(){
        currentShape.Update();
    }

    @Override
    public void paint(Graphics g) {

        currentShape.Render(g);

        for (int x = marge; x <= width_grid; x += size1Block) {
            for (int y = marge ; y <= height_grid; y += size1Block) {
                g.setColor(Color.black);
                g.drawRect(x, y, size1Block, size1Block);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                currentShape.setDeltaX(-1); //1 block to the left, - direction
                break;
            case KeyEvent.VK_RIGHT:
                currentShape.setDeltaX(1); //1 block to the right, + direction
                break;
            case KeyEvent.VK_DOWN:
                currentShape.setSpeedDown(); //will accelerate going down
                break;
            case KeyEvent.VK_UP:
                currentShape.Rotate(); //rotate the pentomino
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) { //we need to stop infinity going down
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            currentShape.setNormalSpeed();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} //not needed


    public int getSize1Block(){return size1Block;}
    public int getMarge() {return marge;}
    public int getxAxis() {return xAxis;}
    public int getyAxis() {return yAxis;}

}
