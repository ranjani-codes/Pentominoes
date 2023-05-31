import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Grid extends JPanel implements KeyListener {
    private final int size1Block = 40; //Size by pixel per block
    private final int xAxis = 10; //Number of x boxes
    private final int yAxis = 20; //Number of y boxes

    private int width_grid = size1Block*xAxis, height_grid = size1Block*yAxis; //Total grid dimensions
    private int marge = 10; //Pixels from the border before the grid
    private int [][] grid = new int[xAxis][yAxis]; //Array of the grid

    private PentominoShapes[]  pentominoShapes = new PentominoShapes[12];//12 shapes in total
    private PentominoShapes currentShape;

    private Timer timer;
    private final int fps = 60;//Refresh screen per second
    private int delay = (int)(1000/fps);//Delay/time between each refreshment


    public Grid(){

        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Update();
                repaint(); //Repaint fps(60)/sec
            }
        });

        timer.start();

        //Shapes:
        //L
        pentominoShapes[0] = new PentominoShapes(new int[][]{{0,0,0,1,0},{1,1,1,1,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{25,190,155},this);
        //P
        pentominoShapes[1] = new PentominoShapes(new int[][]{{1,1,0,0,0},{1,1,0,0,0},{1,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{45,205,115},this);
        //X
        pentominoShapes[2] = new PentominoShapes(new int[][]{{0,1,0,0,0},{1,1,1,0,0},{0,1,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{50,150,220},this);
        //F
        pentominoShapes[3] = new PentominoShapes(new int[][]{{0,1,1,0,0},{1,1,0,0,0},{0,1,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{95,90,180},this);
        //V
        pentominoShapes[4] = new PentominoShapes(new int[][]{{1,0,0,0,0},{1,0,0,0,0},{1,1,1,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{50,75,95},this);
        //W
        pentominoShapes[5] = new PentominoShapes(new int[][]{{1,0,0,0,0},{1,1,0,0,0},{0,1,1,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{240,195,15},this);
        //Y
        pentominoShapes[6] = new PentominoShapes(new int[][]{{0,1,0,0,0},{1,1,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,0,0,0,0}}, new int []{230,125,35},this);
        //I
        pentominoShapes[7] = new PentominoShapes(new int[][]{{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0}}, new int []{230,75,60},this);
        //T
        pentominoShapes[8] = new PentominoShapes(new int[][]{{1,1,1,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{150,165,165},this);
        //Z
        pentominoShapes[9] = new PentominoShapes(new int[][]{{1,1,0,0,0},{0,1,0,0,0},{0,1,1,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{245,155,20},this);
        //U
        pentominoShapes[10] = new PentominoShapes(new int[][]{{1,0,1,0,0},{1,1,1,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}}, new int []{210,85,0},this);
        //N
        pentominoShapes[11] = new PentominoShapes(new int[][]{{1,1,0,0,0},{0,1,1,1,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}},new int []{190,57,43},this);

        currentShape = pentominoShapes[3];
    }


    public void Update(){
        currentShape.Update();
    }

    @Override
    public void paint(Graphics g) {

        currentShape.Render(g);

        for (int x = marge; x <= width_grid; x += size1Block) { //10 is for the marge before the grid
            for (int y = marge ; y <= height_grid; y += size1Block) { //10 is for the marge before the grid
                g.setColor(Color.black);
                g.drawRect(x, y, size1Block, size1Block);
            }
        }
    }

    public int getSize1Block(){return size1Block;}
    public int getMarge() {return marge;}
    public int getxAxis() {return xAxis;}
    public int getyAxis() {return yAxis;}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                System.out.println("LEFT");
                currentShape.setDeltaX(-1); //1 block to the left, - direction
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("RIGHT");
                currentShape.setDeltaX(1); //1 block to the right, + direction
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("DOWN");
                currentShape.setSpeedDown(); //1 block to the left, - direction
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { //Needed to stop infinity going down
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            currentShape.setNormalSpeed();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} //Not needed


}
