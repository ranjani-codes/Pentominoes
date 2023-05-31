import java.util.*;

public class Branching {
  public static final int horizontalGridSize = 5;
  public static final int verticalGridSize = 12;

  public static final char[] input = {'P', 'X', 'F', 'V', 'W', 'Y', 'I', 'T', 'Z', 'U', 'N', 'L'};
  public static int counter;

public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);


//initialises grid/field
private static boolean search() {
  int[][] grid = new int[horizontalGridSize][verticalGridSize];
  for(int i=0; i<grid.length; i++) {
    for(int j=0; j<grid[0].length; j++) {
      grid[i][j] = -1;
      }
    }
  return algHeart(grid, 0);
  }

  public static boolean isOverlapping(int[][] initField, int[][] pieceToPlace, int x, int y){
    //If there is a possibility to place the piece on the field, return false
    int cntr = 0;
    for (int m = 0; m < pieceToPlace.length; m++)
    {
      for (int j = 0; j < pieceToPlace[m].length; j++)
      {
        if (pieceToPlace[m][j] == 1)
        {
          if (initField[x+m][y+j] == -1)
          {
            cntr++;
          }
        }
      }
    }
    if (cntr == 5)
    {
      return false;
    }
    return true;
  }

//checks if piece is overlapping anything before adding it
private static int[][] addPiece (int[][] grid, int[][] piece, int id, int x, int y) {
  int[][] copy = grid;

  for(int i=0; i<piece.length; i++) {
    for (int j = 0; j < piece[0].length; j++) {
      if (piece[i][j] == 1) {
        copy[x + i][y + j] = id;
      }
    }
  }

  return copy;
}

private static int emptyArea(ArrayList<Coordinates> toBeChecked) {
  int area = 0;
  int number = 0;
  int x = toBeChecked.get(number).x;
  int y = toBeChecked.get(number).y;
}

private static boolean smallestHole(int[][] grid) {
  ArrayList<Coordinates> toBeChecked = new ArrayList<>();
  for (int i = 0; i < grid.length; i++) {
    for (int j = 0; j < grid[0].length; j++) {
      if(grid[i][j] == -1) {
        Coordinates pair = new Coordinates(i, j);
        toBeChecked.add(pair);
      }
    }
  }
  boolean starting = true;

  return true;
}



private static ArrayList<int[][]> genPossib(int[][] grid,int id) {
  ArrayList<int[][]> possibPos = new ArrayList<int[][]>();

  for(int i=0; i<PentominoDatabase.data[id].length; i++) {
    int[][] piece = PentominoDatabase.data[id][i];
    for(int j=0; j<grid.length - piece.length; j++) {
      for(int k=0; k<grid[0].length - piece[0].length; k++) {
        int[][] gridCopy = new int[grid.length][grid[0].length];
        for(int z=0; z<grid.length; z++) {
          for(int s=0; s<grid[0].length; s++) {
            gridCopy[z][s] = grid[z][s];
          }
        }
        if(!isOverlapping(grid, piece, j,k)) {
          possibPos.add(addPiece(gridCopy, piece, id, j, k));
          //System.out.println(possibPos.size());
        }
      }
    }
  }
  return possibPos;
}

private static boolean algHeart(int[][] grid, int layer) {
  smallestHole(grid);
  return true;
}

private static int characterToID(char character) {

  int pentID = -1;

  if (character == 'X') {
    pentID = 0;
    }
  else if (character == 'I') {
    pentID = 1;
    }
  else if (character == 'Z') {
    pentID = 2;
    }
  else if (character == 'T') {
    pentID = 3;
    }
  else if (character == 'U') {
    pentID = 4;
    }
  else if (character == 'V') {
    pentID = 5;
    }
  else if (character == 'W') {
    pentID = 6;
    }
  else if (character == 'Y') {
    pentID = 7;
    }
  else if (character == 'L') {
    pentID = 8;
    }
  else if (character == 'P') {
    pentID = 9;
    }
  else if (character == 'N') {
    pentID = 10;
    }
  else if (character == 'F') {
    pentID = 11;
    }
  return pentID;
  }



  public static void main(String[] args)
  {
    long startTime = System.nanoTime();

    boolean solution = search();

    long endTime = System.nanoTime();
    long timeElapsed = endTime - startTime;
    if(!solution){
      System.out.println("No solution");
    }
    else{
      System.out.println("Solution found");
      System.out.println("Time taken: " + timeElapsed / 1000000+" ms");
    }


  }

}
