import java.awt.*;
import java.awt.image.BufferedImage;

public class PentominoShape {

	private int[][] coords;
	private Grid board;
	private int deltaX = 0;
	private int x, y;

	private int color;

	private boolean collision = false, moveX = false;

	private int normalSpeed = 600, speedDown = 60, currentSpeed;


	private long time, lastTime;

	public PentominoShape( int[][] coords, int color, Grid board){
		this.coords = coords;
		this.board = board;
		this.color = color;

		currentSpeed = normalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis();

		x = 3;
		y = 0;

	}

	public void update(){
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		if(collision)
		{
			for(int row = 0; row < coords.length; row++)
				for(int col = 0; col < coords[row].length; col++)
					if(coords[row][col] != 0)
						board.getBoard()[y + row][x + col] = color;

			checkLine();
			board.setNextShape();
		}

		if(!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0))
		{

			for(int row = 0; row < coords.length; row++)
				for(int col = 0; col < coords[row].length; col++)
					if(coords[row][col] != 0)
					{
						if(board.getBoard()[y + row][x + deltaX + col] != 0)
							moveX = false;
					}
			if(moveX)
				x += deltaX;
		}


		if(!(y + 1 + coords.length > 20))
		{

			for(int row = 0; row < coords.length; row++)
				for(int col = 0; col < coords[row].length; col++)
					if(coords[row][col] != 0)
					{
						if(board.getBoard()[y + row + 1][col + x] != 0)
							collision = true;
					}
			if(time > currentSpeed)

			{
				y++;
				time = 0;
			}
		}else{
			collision = true;
		}

		deltaX = 0;
		moveX = true;
	}

	public void render(Graphics g){

		for(int row = 0; row < coords.length; row++)
			for(int col = 0 ; col < coords[row].length; col++)
				if(coords[row][col] != 0) {
					int[] colorArray = new ColorPentomino(board.getIndex()).GiveColor();
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
					g.fillRect(col * board.getBlockSize() + x * board.getBlockSize(), row * board.getBlockSize() + y * board.getBlockSize(),board.getBlockSize(),board.getBlockSize() );


				}
	}

	private void checkLine(){
		int height = board.getBoard().length - 1;

		for(int i = height; i > 0; i--){

			int count = 0;
			for(int j = 0; j < board.getBoard()[0].length; j++){

				if(board.getBoard()[i][j] != 0)
					count ++;

				board.getBoard()[height][j] = board.getBoard()[i][j];

			}
			if(count < board.getBoard()[0].length)
				height --;


		}


	}


	public void rotate(){

		if(collision)
			return;

		int[][] rotatedMatrix = null;

		rotatedMatrix = getTranspose(coords);

		rotatedMatrix = getReverseMatrix(rotatedMatrix);

		if(x + rotatedMatrix[0].length > 10 || y + rotatedMatrix.length > 20)
			return;

		for(int row = 0; row < rotatedMatrix.length; row++)
		{
			for(int col = 0; col < rotatedMatrix[0].length; col++){

				if(board.getBoard()[y + row][x + col] != 0){
					return;
				}

			}
		}

		coords = rotatedMatrix;

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

	public void normalSpeed(){
		currentSpeed = normalSpeed;
	}


	public void speedDown(){
		currentSpeed = speedDown;
	}


	public int[][] getCoords() {
		return coords;
	}
	public int getColor(){
		return color;
	}

}
