import java.awt.*;

public class PentominoShapes {

    private int [][] position;//coordinates of a pentomino
    private int [] color;//Color of the pentomino
    private Grid grid; //make an object grid to access the class Grid
    private int deltaX = 0;
    private int xPos = 3; //Middle of the grid of 10
    private int yPos = 0; //On the top of the grid


    private int normalSpeed = 600, speedDownKey = 80, currentSpeed;//Speeds of normal fall down frequency and when pressing the Down key to accelerate the fall
    private long time, lastTime;


    //Constructor
    public PentominoShapes(int [][] position, int [] color, Grid grid){
        this.position = position;
        this.color = color;
        this.grid = grid;

        currentSpeed = normalSpeed;
        time = 0; //Set time to 0 when you are calling the function
        lastTime = System.currentTimeMillis(); //copy the current time in milliseconds into the variable
    }


    public void Update() {

        time += System.currentTimeMillis() - lastTime; //The current time that passed
        lastTime = System.currentTimeMillis();

        //X:
        //calculate the width of the piece (1's in the array)
        int width_piece = 0;
        for (int i = 0; i < position[0].length; i++){
            for (int j = 0; j < position.length; j++){
                if (position[i][j] == 1){
                    width_piece++;
                    break;
                }
            }
        }
        //Keep the piece inside the grid
        if ((xPos + deltaX + width_piece <= grid.getxAxis()) && (xPos + deltaX >= 0)) { //using width_piece, because if you dont calculate it, "I" has another length value than "P"
            xPos += deltaX; //+1 or -1 if pressed right or left
        }

        //Y:
        //calculate the height of the piece (1's in the array)
        int height_piece = 1;
        for (int i = 0; i < position[0].length; i++){
            for (int j = 0; j < position.length; j++){
                if (position[j][i] == 1){
                    height_piece++;
                    break;
                }
            }
        }
        if (yPos + height_piece <= grid.getyAxis()) {
            if (time > currentSpeed) { //The time that passed is > the frequency time we want it goes down, go down
                yPos++; //Go 1 block down
                time = 0;//Reset time
            }
        }


        deltaX = 0;//Reset
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


    public void Rotate(){ //First we need to transpose the matrix(exchange rows and collumns), Then reverse rows(Left rotation) or collums(Right rotations), we will rotate left, the rows
        int [][] rotatedMatrix = null; //empty matrix

        rotatedMatrix = TransposeMatrix(position);

        rotatedMatrix = ReverseMatrix(rotatedMatrix);

        if ((xPos + rotatedMatrix[0].length > grid.getyAxis()) || (yPos + rotatedMatrix.length > grid.getyAxis())){
            return;
        }
        position = rotatedMatrix;

    }

    private int [][] TransposeMatrix(int [][] matrix ){
        int [][] tempmatrix = new int [matrix.length][matrix[0].length]; //make a new temporary matrix that will store the transposed matrix

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                tempmatrix[j][i] = matrix[i][j]; //each time switch the position between their opposite. ex:{{1,2,3},{4,5,6},{7,8,9}} -> {{1,4,7},{2,5,8},{3,6,9}}
            }
        }
        matrix = tempmatrix;
        return matrix;
    }

    private int [][] ReverseMatrix(int [][] matrix){
        int middle = matrix.length/2; //stop at the middle of the matrix, we do a horizontal miirroring
        int [][] tempmatrix = new int [matrix.length][matrix[0].length]; //make a new temporary matrix that will store the reverse matrix

        for(int i = 0; i < middle; i++) {
            for(int j = 0; j < matrix[i].length; j++) { //let's say for the first line (comments):
                tempmatrix[i][j] = matrix[matrix.length -1 - i][j]; //copy the last line of the matrix to the first line of the temp matrix
                tempmatrix[matrix.length -1 - i][j] = matrix[i][j]; //copy the first line of the matrix to the last one of the tempmatrix
                tempmatrix[middle][j] = matrix[middle][j]; //copy the middle line
            }
        }
        matrix = tempmatrix;
        return matrix;
    }


    public void setSpeedDown(){currentSpeed = speedDownKey;}
    public void setNormalSpeed(){currentSpeed = normalSpeed;}
    public void setDeltaX(int deltaX){this.deltaX = deltaX;}

}
