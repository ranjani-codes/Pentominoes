import java.awt.*;
import java.util.*;

public class PentominoShapes {

    private int [][] position; //coordinates of a pentomino
    private int colorN;//Color of the pentomino = shapeN+1
    private int shapeN; //colorN -1
    private Grid grid; //make an object grid to access the class Grid
    private int deltaX = 0;
    private int xPos = 3; //middle of the grid of 10 + POSITION TOP LEFT SHAPE
    private int yPos = 0; //on the top of the grid + POSITION TOP LEFT SHAPE

    private int rotatedMatrix [][] = null;

    private boolean collisionY = false; //collision on the y axis, height, vertical
    private boolean collisionX = false; //collision on the x axis, width, horizontal

    private int normalSpeed = 1000, speedDownKey = 80, currentSpeed; //speeds of normal fall down frequency and when pressing the Down key to accelerate the fall
    private long time, lastTime;


    //Constructor
    public PentominoShapes(int [][] position, int colorN, Grid grid){
        this.position = position; //assign position pentomino
        this.colorN = colorN; //assign color pentomino
        this.grid = grid;  //assign the current state of the grid
        this.shapeN = colorN-1; //we started giving colors with 1, cuz 0 will give us bugs

        currentSpeed = normalSpeed;
        time = 0; //set time to 0 when you are calling the function
        lastTime = System.currentTimeMillis(); //copy the current time in milliseconds into the variable
    }


    public void Update() {

        time += System.currentTimeMillis() - lastTime; //the current time that passed
        lastTime = System.currentTimeMillis(); //"reset" the lastTime to the current time

        //If there is a collisionY, add the pentomino to the grid and generate a new shape
        if (collisionY) { //This will only be activated if we can't do down and move the pentomino left or right
            System.out.println(collisionY);

            for (int y = 0; y < position.length; y++) {
                for (int x = 0; x < position[y].length; x++) {
                    //if the pentomino has !0 values, add the colorN of that pentomino to the grid on the grid
                    if (position[y][x] != 0) {
                        grid.getGrid()[yPos + y][xPos + x] = colorN;
                    }
                }
            }
            Checkline(); //checks if there is a full row by placing the pentomino
            grid.nextPentomino(); //generates a new pentomino
        }

        //X: Keep the pentomino inside the grid, horizontal X sides
        if (MoveLegal("checkLeftorRight")) {
            //if we are able to move the pentomino 1 step further, move it BUT check if there are no pieces left or right of it
            for (int y = 0; y < position.length; y++) {
                for (int x = 0; x < position[y].length; x++) {
                    if (position[y][x] != 0) { //takes only the visible blocks of the pentomino matrix
                        //checks if there is a pentomino left or right (meaning the grid has !0 values), if so -> set collisionX = true.
                        if ((grid.getGrid()[yPos + y][xPos + x + deltaX]) != 0){
                            collisionX = false; //always be able to move until the program comes here, see the "if" statement underneath
                        }
                    }
                }
            }
            if (collisionX){ //if it's true we can move the block
                xPos += deltaX; //+1 or -1 if pressed right or left
                //EXPERIMENTAL REMOVED //collisionY = false;
            }
        }

        //Y: Keep the pentomino inside the grid Y sides
        if (MoveLegal("checkDown")) {

            for (int y = 0; y < position.length; y++) {
                for (int x = 0; x < position[y].length; x++) {
                    if (position[y][x] != 0) {//takes only the visible blocks of the pentomino matrix
                        //if the pentomino has !0 values, set collisionY to true
                        if (grid.getGrid()[yPos + y + 1][x + xPos] != 0) { //+1 because it's gonna check the block underneath
                        collisionY = true; //if there is a block underneath, set the collision to true
                        }
                    }
                }
            }

            if (time > currentSpeed) { //the time that passed is > the frequency time we want it goes down, go down
                yPos++; //go 1 block down
                time = 0; //reset time because we will repeat this time cycle for going down
                // EXPERIMENTAL collisionY = false;
            }

        } else{
            collisionY = true; //when the pentomino cannot be rotated nor moved
        }

        deltaX = 0; //reset the direction moved
        collisionX = true; //always able to move until horizontal collision

    }


    public void Render(Graphics g){

        //Shows the generated pentomino and it's color on the grid
        for (int y = 0; y < position.length; y++) {
            for (int x = 0; x < position[y].length; x++) {
                if (position[y][x] != 0) { //only shows the shape of the pentomino in its matrix
                    //going to show each color of each generated piece
                    int [] colorArray = new ColorPentomino(shapeN).GiveColor(); //take the color array of the pentomino
                    g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the new color
                    g.fillRect((grid.getMarge() + x*grid.getSize1Block() +xPos*grid.getSize1Block()), (grid.getMarge() + y*grid.getSize1Block() + yPos*grid.getSize1Block()), grid.getSize1Block() , grid.getSize1Block());
                }
            }
        }
    }

    //Checks if a line if full or not
    private void Checkline(){
        int y = grid.getGrid().length-1; //-1 because if you are on the top of the grid you lose ofc

        for (int i = y; i > 0; i--){ //start to check from the top of the grid
            int count = 0; //will count how many blocks are filled per row
            for (int j = 0; j < grid.getGrid()[0].length; j++){ //start from the given row and xpos 0
                if (grid.getGrid()[i][j] != 0){ //if the grid is != 0, it means there is a piece
                    count++; //add a "yes there is a filled block" to the counter
                }
                grid.getGrid()[y][j] = grid.getGrid()[i][j]; //if the line is not complete it will stay the same
            }                                                //if the line is complete the line above will be copied to the completed line
            if (count < grid.getGrid()[0].length){ //if the line wasn't full, go check the next one by going 1 down
                y--;
            }
        }
    }

    //Rotate the pentomino
    public void Rotate(){
        if(collisionY) { //if true, do not rotate the piece
            return;
        }

        rotatedMatrix = null; //empty matrix
        rotatedMatrix = RotateMatrix(position); //assign the new rotated matrix

        if (!MoveLegal("checkInGrid")) { //check if we rotate, that the pentomino stays in the grid
            return; //will not rotate the pentomino if doesn't fit in the grid, no position coordinates changed
        }

        //Avoid overlapping when rotating
        for (int y = 0; y <rotatedMatrix.length; y++){
            for (int x = 0; x < rotatedMatrix[0].length; x++){
                if (grid.getGrid()[yPos + y][xPos + x] != 0){
                   return;
                }
            }
        }
        position = rotatedMatrix; //changes the old position matrix to the new one;
    }

    //First we need to transpose the matrix(exchange rows and columns), Then reverse rows(Left rotation) or columns(Right rotations), we will rotate left, the rows
    private int [][] RotateMatrix(int [][] matrix ){
        int[][] newMatrix = new int[matrix[0].length][matrix.length];

        //Transversing the matrix
        for(int y = 0; y < matrix.length; y++){
            for(int x = 0; x < matrix[y].length; x++){
                newMatrix[x][y] = matrix[y][x]; //each time switch the position between their opposite. ex:{{1,2,3},{4,5,6},{7,8,9}} -> {{1,4,7},{2,5,8},{3,6,9}}
            }
        }
        //Reversing the matrix:
        int middle = newMatrix.length / 2; //stop at the middle of the matrix, we do a horizontal mirroring

        for(int y = 0; y < middle; y++){ //Perform the following actions until the middle
            int[] tempMatrix = newMatrix[y]; //row i is copied in array m (kinda backup of the row)
            newMatrix[y] = newMatrix[newMatrix.length - y - 1]; //the normal row is replaced by it's opposite row directly in the matrix (replace the backuped line)
            newMatrix[newMatrix.length - y - 1] = tempMatrix; //assign the backuped line to it's opposite
        }
        return newMatrix;
    }

    private boolean MoveLegal(String use){
        //Checks if we don't go out of the grid horizontally
        if ((use.equals("checkLeftorRight")) && (xPos + deltaX +  position[0].length <= grid.getxAxis()) && (xPos + deltaX  >= 0) && (yPos + position.length < grid.getyAxis())) {
            return true;
        //Checks if we don't go out of the grid horizontally
        } else if ((use.equals("checkDown")) && (yPos + position.length + 1 <= grid.getyAxis())){
            return true;
        //even if we rotate, trill in grid?
        } else if ((use.equals("checkInGrid")) && ((xPos + rotatedMatrix[0].length <= grid.getxAxis()) && (yPos + rotatedMatrix.length <= grid.getyAxis()) && (yPos + position.length < grid.getyAxis()))){
            return true;
        }else
            return false;
    }

    public void setSpeedDown(){currentSpeed = speedDownKey;}
    public void setNormalSpeed(){currentSpeed = normalSpeed;}
    public void setDeltaX(int deltaX){this.deltaX = deltaX;}
    public int[][] getPosition() {return position;}
}
