import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class PentominoShapes {

    private int [][] position; //coordinates of a pentomino
    private int [] color; //Color of the pentomino
    private Grid grid; //make an object grid to access the class Grid
    private int deltaX = 0;
    private int xPos = 3; //middle of the grid of 10
    private int yPos = 0; //on the top of the grid

    private int rotatedMatrix [][] = null;

    private boolean collision = false;

    private int normalSpeed = 2000, speedDownKey = 80, currentSpeed; //speeds of normal fall down frequency and when pressing the Down key to accelerate the fall
    private long time, lastTime;


    //Constructor
    public PentominoShapes(int [][] position, int [] color, Grid grid){
        this.position = position; //assign position pentomino
        this.color = color; //assign color pentomino
        this.grid = grid;  //assign the current state of the grid

        currentSpeed = normalSpeed;
        time = 0; //set time to 0 when you are calling the function
        lastTime = System.currentTimeMillis(); //copy the current time in milliseconds into the variable
    }


    public void Update() {

        time += System.currentTimeMillis() - lastTime; //the current time that passed
        lastTime = System.currentTimeMillis(); //"reset" the lastTime to the current time

        //If there is a collision, generate a new shape
        if (collision){
            grid.nextPiece();
        }

        //Keep the piece inside the grid X sides
        if (MoveLegal("checkLeftorRight")) {
            xPos += deltaX; //+1 or -1 if pressed right or left
        }

        //Keep the piece inside the grid Y sides
        if (MoveLegal("checkDown")) {
            if (time > currentSpeed) { //the time that passed is > the frequency time we want it goes down, go down
                yPos++; //go 1 block down
                time = 0; //reset time
            }
        }else {
            collision = true;
        }

        deltaX = 0; //reset the direction moved
    }


    public void Render(Graphics g){

        for (int y = 0; y < position.length; y++) {
            for (int x = 0; x < position[y].length; x++) {
                if (position[y][x] != 0) {
                    g.setColor(new Color(color[0], color[1], color[2]));
                    g.fillRect((grid.getMarge() + x*grid.getSize1Block() +xPos*grid.getSize1Block()), (grid.getMarge() + y*grid.getSize1Block() + yPos*grid.getSize1Block()), grid.getSize1Block() , grid.getSize1Block());
                }
            }
        }
    }


    public void Rotate(){ //First we need to transpose the matrix(exchange rows and columns), Then reverse rows(Left rotation) or columns(Right rotations), we will rotate left, the rows
        rotatedMatrix = null; //empty matrix

        rotatedMatrix = RotateMatrix(position); //assign the new rotated matrix

        if (!MoveLegal("checkInGrid")) { //check if we rotate, that the piece stays in the grid
            return; //will not rotate the piece if doesn't fit in the grid, no position coordinates changed
        }

        position = rotatedMatrix; //changes the old position matrix to the new one;
    }


    private int [][] RotateMatrix(int [][] matrix ){
        int[][] newMatrix = new int[matrix[0].length][matrix.length];

        //Transversing the matrix
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                newMatrix[j][i] = matrix[i][j]; //each time switch the position between their opposite. ex:{{1,2,3},{4,5,6},{7,8,9}} -> {{1,4,7},{2,5,8},{3,6,9}}
            }
        }
        //Reversing the matrix:
        int middle = newMatrix.length / 2; //stop at the middle of the matrix, we do a horizontal mirroring

        for(int i = 0; i < middle; i++){ //Perform the following actions until the middle
            int[] tempMatrix = newMatrix[i]; //row i is copied in array m (kinda backup of the row)
            newMatrix[i] = newMatrix[newMatrix.length - i - 1]; //the normal row is replaced by it's opposite row directly in the matrix (replace the backuped line)
            newMatrix[newMatrix.length - i - 1] = tempMatrix; //assign the backuped line to it's opposite
        }
        return newMatrix;
    }

    private boolean MoveLegal(String use){
        if ((use.equals("checkLeftorRight")) && (xPos + deltaX +  position[0].length <= grid.getxAxis()) && (xPos + deltaX  >= 0) && (yPos + position.length < grid.getyAxis())) {
            return true;
        } else if ((use.equals("checkDown")) && (yPos + position.length + 1 <= grid.getyAxis())){
            return true;
        } else if ((use.equals("checkInGrid")) && ((xPos + rotatedMatrix[0].length <= grid.getxAxis()) && (yPos + rotatedMatrix.length <= grid.getyAxis()) && (yPos + position.length < grid.getyAxis()))){
            return true;
        }else
            return false;
    }

    public void setSpeedDown(){currentSpeed = speedDownKey;}
    public void setNormalSpeed(){currentSpeed = normalSpeed;}
    public void setDeltaX(int deltaX){this.deltaX = deltaX;}

}
