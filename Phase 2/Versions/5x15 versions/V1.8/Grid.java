import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Grid extends JPanel implements KeyListener {

	private final int xAxis = 5; //number of x boxes
	private final int yAxis = 15; //number of y boxes
	public static int numberOfPentominos = 12; //number of pentominoShapes

	private int marge = 20; //pixels from the border before the grid
	private final int size1Block = 60; //size by pixel per block
	private int width_grid = size1Block * xAxis;  //total width grid dimensions
	private int height_grid = size1Block * yAxis; //total height grid dimensions

	public int[][] grid = new int[yAxis][xAxis]; //array of the grid
	public static PentominoShape[] pentominoShapes = new PentominoShape[numberOfPentominos]; //12 pentomino's in total

	private PentominoShape currentShape;
	public PentominoShape nextShape = initialise();
	private int nextShapeN;

	public Timer timer; //deals with all the timer stuff
	private final int fps = 60; //refresh screen per second
	private int delay = (int) (1000 / fps); //delay/time between each refreshment

	private boolean gameOver = false;
	public int linesCleared = 0;
	public int score = 0;

	public Grid() {

		timer = new Timer(delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Update(); //update will call the Update in the PentominoShape class
				repaint(); //repaint fps(60)/sec
			}
		});

		timer.start();

		//Shapes
		pentominoShapes[0] = new PentominoShape(new int[][]{{0, 0, 0, 1}, {1, 1, 1, 1}}, 1, this);
		//P
		pentominoShapes[1] = new PentominoShape(new int[][]{{1, 1}, {1, 1}, {1, 0}}, 2, this);
		//X
		pentominoShapes[2] = new PentominoShape(new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 3, this);
		//F
		pentominoShapes[3] = new PentominoShape(new int[][]{{0, 1, 1}, {1, 1, 0}, {0, 1, 0}}, 4, this);
		//V
		pentominoShapes[4] = new PentominoShape(new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}}, 5, this);
		//W
		pentominoShapes[5] = new PentominoShape(new int[][]{{1, 0, 0}, {1, 1, 0}, {0, 1, 1}}, 6, this);
		//Y
		pentominoShapes[6] = new PentominoShape(new int[][]{{0, 1}, {1, 1}, {0, 1}, {0, 1}}, 7, this);
		//I
		pentominoShapes[7] = new PentominoShape(new int[][]{{1, 1, 1, 1, 1}}, 8, this);
		//T
		pentominoShapes[8] = new PentominoShape(new int[][]{{1, 1, 1}, {0, 1, 0}, {0, 1, 0}}, 9, this);
		//Z
		pentominoShapes[9] = new PentominoShape(new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 1, 1}}, 10, this);
		//U
		pentominoShapes[10] = new PentominoShape(new int[][]{{1, 0, 1}, {1, 1, 1}}, 11, this);
		//N
		pentominoShapes[11] = new PentominoShape(new int[][]{{1, 1, 0, 0}, {0, 1, 1, 1}}, 12, this);

		nextPentomino();
	}

	public PentominoShape initialise(){
		nextShapeN = (int)(Math.random()*numberOfPentominos); //going in the RandomGenerator class to pick a new number
		nextShape = pentominoShapes[nextShapeN];

		return nextShape;
	}


	//generates a new pentomino number
	public void nextPentomino() {

		currentShape = nextShape; //assign a number to the pentomino
		System.out.println(nextShapeN);

		int[][] position = pentominoShapes[nextShapeN].getPos();
		PentominoShape newShape = new PentominoShape(position, nextShapeN + 1, this);

		currentShape = newShape;

		int [][] nextPos = pentominoShapes[nextShapeN].getPos();
		for (int y = 0; y < nextPos.length; y++){
			for (int x = 0; x < nextPos[0].length; x++){
				if (position[y][x] != 0){ //only look at the 1's in the matrix
					if (grid[y][x] != 0){//+2 because it's just before the top of grid, play with this parameter to see the difference
						gameOver = true; //meaning we reached the top and it will stop the game
					}
				}
			}
		}
			initialise();
	}


	public void Update() {
		currentShape.update(); //take the currentShape and go to the PentominoShape class with it
		if (gameOver) {
			timer.stop(); //stop generating pieces

		}
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		currentShape.Render(g); //take the currentShape and render it in the PentominoShape class
		//Score text

		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				if (grid[y][x] != 0) {
					int[] colorArray = new ColorPentomino(grid[y][x]-1).GiveColor();
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
					g.fillRect(marge + x * size1Block, marge + y * size1Block, size1Block, size1Block); //fill the rectangle
				}
			}
		}

		//Draw the grid
		for (int x = marge; x <= width_grid; x += size1Block) {
			for (int y = marge; y <= height_grid; y += size1Block) {
				g.setColor(Color.black); //lines of the grid are black
				g.drawRect(x, y, size1Block, size1Block); //will draw the rectangle without filling it. only the borders of it will be drawn
			}
		}
	}



	@Override
	//Events when a key is pressed
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				currentShape.setDeltaX(-1); //1 block to the left, - direction
				break;
			case KeyEvent.VK_RIGHT:
				currentShape.setDeltaX(1); //1 block to the right, + direction
				break;
			case KeyEvent.VK_DOWN:
				currentShape.setFastSpeed(); //will accelerate going down
				break;
			case KeyEvent.VK_UP:
				currentShape.Rotate(); //rotate the pentomino
				break;
		}
	}

	@Override
	public void keyReleased (KeyEvent e){ //we need to stop the block to go infinitely  down
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.setNormalSpeed(); //reset the speed to the normal speed
		}
	}

	@Override
	public void keyTyped(KeyEvent e){} //not needed, but need to be written. it's part of the "implements KeyListener"

	public PentominoShape getNext(){ return nextShape;}
	public int getScore(){ return score;}
	public int getLinesCleared(){ return linesCleared;}

	public int getNumberOfPentominos() {
		return numberOfPentominos;
	}

	public int getSize1Block() {return size1Block;}
	public int getMarge() {return marge;}
	public int[][] getGrid() {return grid;}
	public int getxAxis() {return xAxis;}
	public int getyAxis() {return yAxis;}

}
