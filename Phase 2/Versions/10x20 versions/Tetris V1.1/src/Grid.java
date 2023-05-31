import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Grid extends JPanel implements KeyListener{

	private final int blockSize = 30;

	private final int boardWidth = 10, boardHeight = 20;

	private int[][] board = new int[boardHeight][boardWidth];

	private PentominoShape[] shapes = new PentominoShape[12];

	private PentominoShape currentShape;

	private Timer timer;

	private final int FPS = 60;

	private final int delay = 1000/FPS;

	private boolean gameOver = false;
	private int index;

	public Grid(){

		timer = new Timer(delay, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
			}
		});

		timer.start();

		// shapess
//L
		shapes[0] = new PentominoShape(new int[][]{{0,0,0,1},{1,1,1,1}}, 1,this);
		//P
		shapes[1] = new PentominoShape(new int[][]{{1,1},{1,1},{1,0}}, 2,this);
		//X
		shapes[2] = new PentominoShape(new int[][]{{0,1,0},{1,1,1},{0,1,0}}, 3,this);
		//F
		shapes[3] = new PentominoShape(new int[][]{{0,1,1},{1,1,0},{0,1,0}}, 4,this);
		//V
		shapes[4] = new PentominoShape(new int[][]{{1,0,0},{1,0,0},{1,1,1}}, 5,this);
		//W
		shapes[5] = new PentominoShape(new int[][]{{1,0,0},{1,1,0},{0,1,1}}, 6,this);
		//Y
		shapes[6] = new PentominoShape(new int[][]{{0,1},{1,1},{0,1},{0,1}}, 7,this);
		//I
		shapes[7] = new PentominoShape(new int[][]{{1,1,1,1,1}}, 8,this);
		//T
		shapes[8] = new PentominoShape(new int[][]{{1,1,1},{0,1,0},{0,1,0}}, 9,this);
		//Z
		shapes[9] = new PentominoShape(new int[][]{{1,1,0},{0,1,0},{0,1,1}}, 10,this);
		//U
		shapes[10] = new PentominoShape(new int[][]{{1,0,1},{1,1,1}}, 11,this);
		//N
		shapes[11] = new PentominoShape(new int[][]{{1,1,0,0},{0,1,1,1}},12,this);
		setNextShape();

	}

	public void update(){
		currentShape.update();
		if(gameOver)
			timer.stop();
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);

		currentShape.render(g);

		for(int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++){
				if (board[row][col] != 0) {
					int[] colorArray = new ColorPentomino(index).GiveColor();
					g.setColor(new Color(colorArray[0], colorArray[1], colorArray[2])); //assign the color that has to be drawn
					g.fillRect(col * blockSize, row * blockSize, blockSize, blockSize); //fill the rectangle
				}
			}
		}


		for (int x = 0; x <= boardWidth*blockSize; x += blockSize) {
			for (int y = 0 ; y <= boardHeight*blockSize; y += blockSize) {
				g.setColor(Color.black); //lines of the grid are black
				g.drawRect(x, y, blockSize, blockSize); //will draw the rectangle without filling it. only the borders of it will be drawn
			}
		}

	}

	public void setNextShape(){

		int index = new RandomGenerator(shapes.length).Generator();
		this.index = index;

		PentominoShape newShape = new PentominoShape(getcoord(index), index+1,
				this);
		//		Shape newShape = new PentominoShape(,index+1,this);

		currentShape = newShape;

		for(int row = 0; row < currentShape.getCoords().length; row++)
			for(int col = 0; col < currentShape.getCoords()[row].length; col++)
				if(currentShape.getCoords()[row][col] != 0){

					if(board[row][col + 3] != 0)
						gameOver = true;
				}



	}


	public int getBlockSize(){
		return blockSize;
	}

	public int[][] getBoard(){
		return board;
	}
	public int getIndex(){return index;}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			currentShape.setDeltaX(-1);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			currentShape.setDeltaX(1);
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.speedDown();
		if(e.getKeyCode() == KeyEvent.VK_UP)
			currentShape.rotate();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentShape.normalSpeed();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public int [][] getcoord(int index){
		switch (index){
			case 0:
				int [][] Color0 ={{0,0,0,1},{1,1,1,1}};
				return Color0;
			case 1:
				int [][] Color1 ={{1,1},{1,1},{1,0}};
				return Color1;
			case 2:
				int [][] Color2 = {{0,1,0},{1,1,1},{0,1,0}};
				return Color2;
			case 3:
				int [][] Color3 = {{0,1,1},{1,1,0},{0,1,0}};
				return Color3;
			case 4:
				int [][] Color4 = {{1,0,0},{1,0,0},{1,1,1}};
				return Color4;
			case 5:
				int [][] Color5 = {{1,0,0},{1,1,0},{0,1,1}};
				return Color5;
			case 6:
				int [][] Color6 = {{0,1},{1,1},{0,1},{0,1}};
				return  Color6;
			case 7:
				int [][] Color7 = {{1,1,1,1,1}};
				return Color7;
			case 8:
				int [][] Color8 = {{1,1,1},{0,1,0},{0,1,0}};
				return Color8;
			case 9:
				int [][] Color9 = {{1,1,0},{0,1,0},{0,1,1}};
				return Color9;
			case 10:
				int [][] Color10 = {{1,0,1},{1,1,1}};
				return Color10;
			case 11:
				int [][] Color11 = {{1,1,0,0},{0,1,1,1}};
				return Color11;

		}		return null;

	}
}
