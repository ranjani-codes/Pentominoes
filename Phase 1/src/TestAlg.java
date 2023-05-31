import java.util.*;

public class TestAlg {

  public static final int horizontalGridSize = 5;
  public static final int verticalGridSize = 12;

  public static final char[] input = { 'P', 'X', 'F', 'V', 'W', 'Y', 'I', 'T', 'Z', 'U', 'N', 'L'};

  public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

  public static int[][] init() {

    int[][] initGrid = new int[horizontalGridSize][verticalGridSize];

    for(int i = 0; i < initGrid.length; i++){
        for(int j = 0; j < initGrid[i].length; j++){
          initGrid[i][j] = -1;
        }
    }
    return initGrid;
  }

  //Add pieces after checking if the move is "Legal"
  private static int[][] addPiece(int[][] grid, int[][] piece, int id, int x, int y) {
    int[][] newGrid = grid;
    int counter = 0;
    boolean noOverlap = true;

    for(int i=0; i<piece.length; i++) {
      for(int k=0; k<piece[i].length; k++) {
        if(piece[i][k] == 1)
          if(grid[x+i][y+k] == -1)
            counter++;
      }
    }
    if(counter == 5)
      noOverlap = true;
    else
      noOverlap = false;
    if(noOverlap == true) {
      for(int i=0; i<piece.length; i++) {
        for(int k=0; k<piece[i].length; k++) {
          if(piece[i][k] == 1)
            newGrid[x+i][y+k] = id;
        }
      }
      return newGrid;
    }
    return null;
  }

  private static int smallestHole(int[][] grid) {
    int smallestArea = (horizontalGridSize * verticalGridSize) + 1;
    int emptySpots = 0;

    for(int[] row: grid) {
      for(int pos: row) {
        if(pos == -1) {
          emptySpots++;
        }
      }
    }
    int[][] gridSearch = new int[grid.length][grid[0].length];
    for(int i=0; i<grid.length; i++) {
      for(int k=0; k<grid[0].length; k++) {
        gridSearch[i][k] = grid[i][k];
      }
    }
    int count = 0;
    while(count != emptySpots) {
      for(int i=0; i<gridSearch.length; i++) {
        for(int k=0; k<gridSearch[0].length; k++) {
          if(gridSearch[i][k] == -1) {
            int area = emptyArea(gridSearch, i, k);
            count += area;
            smallestArea = Math.min(smallestArea, area);
          }
        }
      }
    }
    count = 0;
    return smallestArea;
  }

  //Using the flood fill algorithm we get the size of the empty area at (x,y)
  private static int emptyArea(int[][] grid, int x, int y) {
    int area = 0;

    if(grid[x][y] == -1) {
      grid[x][y] = -2;
      area++;
    }

    if(x!=0)
      if(grid[x-1][y] == -1) {
        area += emptyArea(grid, x-1, y);
      }

    if(y!=0)
      if(grid[x][y-1] == -1) {
        area += emptyArea(grid, x, y-1);
      }

      if(y != grid[0].length-1)
          if(grid[x][y+1] == -1){
              area += emptyArea(grid, x, y+1);
          }

      if(x != grid.length-1)
          if(grid[x+1][y] == -1){
              area += emptyArea(grid, x+1, y);
          }
      return area;
  }

  //This method generates all possible permutations of a piece on this grid
  private static ArrayList<int[][]> genPossib(int[][] grid, int id){
    ArrayList<int[][]> positions = new ArrayList<int[][]>();
    for(int i=0; i<PentominoDatabase.data[id].length; i++) {
      int[][] piece =  PentominoDatabase.data[id][i];
        for(int k=0; k< grid.length - piece.length; k++) {
          for(int j=0; j< grid[0].length - piece[0].length+1; j++) {
            int[][] gridCopy = new int[grid.length][grid[0].length];
            for(int z = 0; z < grid.length; z++){
                for (int s = 0; s < grid[0].length; s++){
                    gridCopy[z][s] = grid[z][s];
                }
            }
            if(addPiece(gridCopy, piece, id, k, j) != null) {
              positions.add(addPiece(gridCopy, piece, id, k, j));
            }
          }
        }
    }
    return positions;
  }

  //This method eliminates illegal moves and goes back to the previous reursive layer to check for another move
  private static boolean eliminateIllegal(int[][] grid, int layer) {
    Random randGen = new Random();
    int nextPent = characterToID(input[layer]);
    ArrayList<int[][]> positions = genPossib(grid, layer);
    ArrayList<int[][]> legalPos = new ArrayList<int[][]>();
    for(int[][] board: positions) {

      if(smallestHole(grid) == (horizontalGridSize*verticalGridSize) +1) {
        ui.setState(board);
        return true;
      }
      else if(smallestHole(grid) >= 5 && smallestHole(grid)%5 == 0) {
        legalPos.add(board);
      }
    }

    int newLayer = layer +1;
    while(!legalPos.isEmpty()) {
      int randSelected = randGen.nextInt(legalPos.size());
      int[][] temp = legalPos.get(randSelected);
      System.out.print(temp.length);
      int[][] nextGrid = new int[temp.length][temp[0].length];
      for(int i=0; i<temp.length; i++) {
        for(int j=0; j<temp[0].length; j++) {
          nextGrid[i][j] = temp[i][j];
        }
      }
      legalPos.remove(randSelected);

      if(eliminateIllegal(nextGrid, newLayer)) {
        return true;
      }
    }
    return false;
  }


  private static int characterToID(char character) {
      int pentID = -1;
      if (character == 'X') {
        pentID = 0;
      } else if (character == 'I') {
        pentID = 1;
      } else if (character == 'Z') {
        pentID = 2;
      } else if (character == 'T') {
        pentID = 3;
      } else if (character == 'U') {
        pentID = 4;
      } else if (character == 'V') {
        pentID = 5;
      } else if (character == 'W') {
        pentID = 6;
      } else if (character == 'Y') {
        pentID = 7;
      } else if (character == 'L') {
        pentID = 8;
      } else if (character == 'P') {
        pentID = 9;
      } else if (character == 'N') {
        pentID = 10;
      } else if (character == 'F') {
        pentID = 11;
      }
      return pentID;
    }

    /*public static void main(String[] args)
    {
        long startTime = System.nanoTime();

        boolean solution = eliminateIllegal(init(),0);

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        if(!solution){
            System.out.println("No solution");
        }
        else{
            System.out.println("Solution found");
            System.out.println("Time taken: " + timeElapsed / 1000000+" ms");
        }
    }*/
}
