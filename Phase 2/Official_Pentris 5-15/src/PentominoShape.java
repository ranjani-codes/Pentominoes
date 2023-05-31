import java.awt.*;

public class PentominoShape {

	private int [][] position; //coordinates of a pentomino
	private Grid grid; //make an object grid to access the class Grid

	private int deltaX = 0;
	private int yPos = 0; //on the top of the grid + POSITION TOP LEFT SHAPE
	private int xPos = 3; //middle of the grid of 10 + POSITION TOP LEFT SHAPE

	private int colorN;//Color of the pentomino = shapeN+1
	private int currentShape; //colorN -1

	private boolean collisionY = false;
	private boolean collisionX = false;

	private int normalSpeed = 1000;
	private int speedDown = 70;
	private int currentSpeed;


	private long time, lastTime;

	public PentominoShape( int[][] position, int colorN, Grid grid){
		this.position = position;
		this.grid = grid;
		this.colorN = colorN;
		this.currentShape = colorN-1;

		currentSpeed = normalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis();

		xPos = 0;
		yPos = 0;
	}

	public void update(){
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		if(collisionY){
			for(int row = 0; row < position.length; row++) {
				for (int col = 0; col < position[row].length; col++) {
					if (position[row][col] != 0) {
						grid.getGrid()[yPos + row][xPos + col] = colorN;
					}
				}
			}
			checkLine();
			grid.nextPentomino();
		}

		if(!(xPos + deltaX + position[0].length > grid.getxAxis()) && !(xPos + deltaX < 0)){
			for(int row = 0; row < position.length; row++){
				for (int col = 0; col < position[row].length; col++){
					if (position[row][col] != 0) {
						if (grid.getGrid()[yPos + row][xPos + deltaX + col] != 0)
							collisionX = false;
					}
				}
			}
			if(collisionX) {
				xPos += deltaX;
			}
		}


		if(!(yPos+ 1 + position.length > grid.getyAxis()))
		{

			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
					{
						if(grid.getGrid()[yPos+ row + 1][col + xPos] != 0)
							collisionY = true;
					}
			if(time > currentSpeed)

			{
				yPos++;
				time = 0;
			}
		}else{
			collisionY = true;
		}

		deltaX = 0;
		collisionX = true;
	}

	public void Render(Graphics g){

		//Shows the generated pentomino and it's color on the grid
		for (int y = 0; y < position.length; y++) {
			for (int x = 0; x < position[y].length; x++) {
				if (position[y][x] != 0) { //only shows the shape of the pentomino in its matrix
					//going to show each color of each generated piece
					int [] colorArray = new ColorPentomino(currentShape).GiveColor(); //take the color array of the pentomino
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the new color
					g.fillRect((grid.getMarge() + x*grid.getSize1Block() +xPos*grid.getSize1Block()), (grid.getMarge() + y*grid.getSize1Block() + yPos*grid.getSize1Block()), grid.getSize1Block() , grid.getSize1Block());
				}
			}
		}
	}


	private void checkLine(){
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


	public void Rotate(){

		if(collisionY)
			return;

		int[][] rotatedMatrix = null;

		rotatedMatrix = RotateMatrix(position);

		if(xPos + rotatedMatrix[0].length > grid.getxAxis() || yPos+ rotatedMatrix.length > grid.getyAxis())
			return;

		for(int row = 0; row < rotatedMatrix.length; row++)
		{
			for(int col = 0; col < rotatedMatrix[0].length; col++){

				if(grid.getGrid()[yPos+ row][xPos + col] != 0){
					return;
				}
			}
		}
		position = rotatedMatrix;

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




	public void setDeltaX(int deltaX){this.deltaX = deltaX;}
	public void setNormalSpeed(){currentSpeed = normalSpeed;}
	public void setFastSpeed(){currentSpeed = speedDown;}
	public int[][] getPos() {return position;}
	public int getColor(){return colorN;}

}
