import java.awt.*;
import java.awt.image.BufferedImage;

public class PentominoShape {

	private int [][] position; //coordinates of a pentomino
	private Grid grid; //make an object grid to access the class Grid

	private int deltaX = 0;
	private int yPos = 0; //on the top of the grid + POSITION TOP LEFT SHAPE
	private int xPos = 3; //middle of the grid of 10 + POSITION TOP LEFT SHAPE

	private int colorN;//Color of the pentomino = shapeN+1

	private boolean collision = false, moveX = false;

	private int normalSpeed = 1000;
	private int speedDown = 60;
	private int currentSpeed;


	private long time, lastTime;

	public PentominoShape( int[][] position, int colorN, Grid grid){
		this.position = position;
		this.grid = grid;
		this.colorN = colorN;

		currentSpeed = normalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis();

		xPos = 3;
		yPos = 0;

	}

	public void update(){
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		if(collision)
		{
			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
						grid.getGrid()[yPos+ row][xPos + col] = colorN;

			checkLine();
			grid.nextPentomino();
		}

		if(!(xPos + deltaX + position[0].length > 10) && !(xPos + deltaX < 0))
		{

			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
					{
						if(grid.getGrid()[yPos+ row][xPos + deltaX + col] != 0)
							moveX = false;
					}
			if(moveX)
				xPos += deltaX;
		}


		if(!(yPos+ 1 + position.length > 20))
		{

			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
					{
						if(grid.getGrid()[yPos+ row + 1][col + xPos] != 0)
							collision = true;
					}
			if(time > currentSpeed)

			{
				yPos++;
				time = 0;
			}
		}else{
			collision = true;
		}

		deltaX = 0;
		moveX = true;
	}

	public void Render(Graphics g){

		for(int row = 0; row < position.length; row++)
			for(int col = 0 ; col < position[row].length; col++)
				if(position[row][col] != 0) {
					int[] colorNArray= new ColorPentomino(grid.getIndex()).GiveColor();
					g.setColor(new Color(colorNArray[0], colorNArray[1], colorNArray[2])); //assign the colorN that has to be drawn
					g.fillRect(grid.getMarge() + col * grid.getBlockSize() + xPos * grid.getBlockSize(), grid.getMarge() + row * grid.getBlockSize() + yPos* grid.getBlockSize(),grid.getBlockSize(),grid.getBlockSize());


				}
	}

	private void checkLine(){
		int height = grid.getGrid().length - 1;

		for(int i = height; i > 0; i--){

			int count = 0;
			for(int j = 0; j < grid.getGrid()[0].length; j++){

				if(grid.getGrid()[i][j] != 0)
					count ++;

				grid.getGrid()[height][j] = grid.getGrid()[i][j];

			}
			if(count < grid.getGrid()[0].length)
				height --;


		}


	}


	public void Rotate(){

		if(collision)
			return;

		int[][] rotatedMatrix = null;

		rotatedMatrix = getTranspose(position);

		rotatedMatrix = getReverseMatrix(rotatedMatrix);

		if(xPos + rotatedMatrix[0].length > 10 || yPos+ rotatedMatrix.length > 20)
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

	private int[][] getTranspose(int[][] matrix){
		int[][] newMatrix = new int[matrix[0].length][matrix.length];

		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[0].length; j++)
				newMatrix[j][i] = matrix[i][j];

		return newMatrix;


	}

	private int[][] getReverseMatrix(int[][] matrix){
		int middle = matrix.length / 2;

		for(int i = 0; i < middle; i++){
			int[] m = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = m;
		}
		return matrix;
	}




	public void setDeltaX(int deltaX){
		this.deltaX = deltaX;
	}

	public void setNormalSpeed(){currentSpeed = normalSpeed;}


	public void setFastSpeed(){
		currentSpeed = speedDown;
	}


	public int[][] getPos() {
		return position;
	}
	public int getColor(){
		return colorN;
	}

}
