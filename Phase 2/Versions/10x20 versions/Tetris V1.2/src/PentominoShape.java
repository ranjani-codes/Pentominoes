import java.awt.*;
import java.awt.image.BufferedImage;

public class PentominoShape {

	private int[][] position;
	private Grid grid;
	private int deltaX = 0;
	private int x, y;

	private int color;

	private boolean collision = false, moveX = false;

	private int normalSpeed = 600, speedDown = 60, currentSpeed;


	private long time, lastTime;

	public PentominoShape( int[][] position, int color, Grid grid){
		this.position = position;
		this.grid = grid;
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
			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
						grid.getGrid()[y + row][x + col] = color;

			checkLine();
			grid.nextPentomino();
		}

		if(!(x + deltaX + position[0].length > 10) && !(x + deltaX < 0))
		{

			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
					{
						if(grid.getGrid()[y + row][x + deltaX + col] != 0)
							moveX = false;
					}
			if(moveX)
				x += deltaX;
		}


		if(!(y + 1 + position.length > 20))
		{

			for(int row = 0; row < position.length; row++)
				for(int col = 0; col < position[row].length; col++)
					if(position[row][col] != 0)
					{
						if(grid.getGrid()[y + row + 1][col + x] != 0)
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

	public void Render(Graphics g){

		for(int row = 0; row < position.length; row++)
			for(int col = 0 ; col < position[row].length; col++)
				if(position[row][col] != 0) {
					int[] colorArray = new ColorPentomino(grid.getIndex()).GiveColor();
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
					g.fillRect(grid.getMarge() + col * grid.getBlockSize() + x * grid.getBlockSize(), grid.getMarge() + row * grid.getBlockSize() + y * grid.getBlockSize(),grid.getBlockSize(),grid.getBlockSize() );


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

		if(x + rotatedMatrix[0].length > 10 || y + rotatedMatrix.length > 20)
			return;

		for(int row = 0; row < rotatedMatrix.length; row++)
		{
			for(int col = 0; col < rotatedMatrix[0].length; col++){

				if(grid.getGrid()[y + row][x + col] != 0){
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
		return color;
	}

}
