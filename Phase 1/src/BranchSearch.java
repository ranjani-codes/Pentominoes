import java.util.*;

public class BranchSearch
{
	public static final int horizontalGridSize = 5;
    public static final int verticalGridSize = 12;

	public static final char[] input = { 'P', 'X', 'F', 'V', 'W', 'Y', 'I', 'T', 'Z', 'U', 'N', 'L'};

    public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

	 public static boolean heuristicDepthFirstSearch() {

				// Initialize an empty board
        int[][] initField = new int[horizontalGridSize][verticalGridSize];

        for(int i = 0; i < initField.length; i++){
            for(int j = 0; j < initField[i].length; j++){
            	initField[i][j] = -1;
            }
        }

        return recursiveRandomBestFirst(initField, 0);
    }

    /// nigward: The heart of the algorithm. You start off with pentimino ID 0 (by setting depth to 0).
    /// generateLayer() is called, generating a list of configurations of `field` with this pentimino
    /// in all possible positions/permutations, stored in the variable `possibilities`.
    ///
    /// We then loop over those possibilities, and calculate the smallest hole left via `minEnclosingArea()`.
    /// If it's not a multiple of five, we can't possible fill that hole with a pentimino, and we can dismiss it.
    /// Imagine for example a row of 6 empty spaces in the top-left corner, with the rest of the board being full.
    /// You can't possibly fill that up completely, since we work in moves of five pieces.
    ///
    /// Once we filtered out impossible boards using the heuristic, stored in a new list called `children`,
    /// we randomly pick out a board from `children`, and call the very same algorithm again using that picked board
    /// as the base, along with the next pentomino ID.
    ///
    /// To summarize: The algo takes a board, finds all possible moves for pentimino 0. Then it randomly picks one of the
    /// generated moves, and finds all possible moves after that move for pentimino 1. Then it does the same for pentimino 2, etc.
    /// It goes through all pentimino IDs, until it fails placing them. Then for example if it fails placing pentomino 2, it goes back up to
    /// pentimino 1, looking at the next move it had generated, and tries placing pentomino 2 everywhere again. That's why it's depth first.
    /// It only goes all the way back to the next move for pentomino 0 after all possible moves for 1, 2, 3... for that move of 0 have been tried.
    private static boolean recursiveRandomBestFirst(int[][] field, int depth) {
        Random random = new Random();

        int nextPentID = characterToID(input[depth]); // takes the first character in the input list

        //generate all possible placements for the next pentomino
       ArrayList<int[][]> possibilities = generateLayer(field, nextPentID);

       //pruning the next layer of the search tree by calling our heuristic function
        ArrayList<int[][]> children = new ArrayList<int[][]>(50);
        //for(int[][] board: possibilities){
        for(int[][] board: possibilities){
            int heuristicMetric = minEnclosingArea(board);
            if(heuristicMetric == ((horizontalGridSize*verticalGridSize)+1)){

                ui.setState(board);
                return true;
							}
            else if((heuristicMetric >= 5) && (heuristicMetric % 5 == 0)){
                //System.out.print(heuristicMetric+", ");
                children.add(board);
            }
        }
        int nextLayer = depth + 1;
        while(!children.isEmpty()){
            int select = random.nextInt(children.size());
            int[][] nextField = children.get(select);
            children.remove(select);
						ui.setState(nextField);
						System.out.println(children.size());
						try
				    {
				      Thread.sleep(100);
				    }
				    catch(InterruptedException ex)
				    {
				      Thread.currentThread().interrupt();
				    }
            if(recursiveRandomBestFirst(nextField, nextLayer))
                return true;
        }
        return false;
    }

    /// nigward: returns a list of modifications of the board `field` containing the pentimino at pentID
    /// in all possible permutations and locations.
    public static ArrayList<int[][]> generateLayer(int[][] field, int pentID){
        ArrayList<int[][]> possibilities = new ArrayList<int[][]>();
        for (int k = 0; k < PentominoDatabase.data[pentID].length; k++){
            int[][] pieceToPlace = PentominoDatabase.data[pentID][k];
            ArrayList<int[][]> permutations = new ArrayList<int[][]>();
            for (int i = 0; i < field.length - pieceToPlace.length + 1; i++){
                for (int j = 0; j < field[0].length - pieceToPlace[0].length + 1; j++){
                    int[][] newField = new int[field.length][field[0].length];
                    for(int l = 0; l < field.length; l++){
                        for (int n = 0; n < field[0].length; n++){
                            newField[l][n] = field[l][n];
                        }
                    }
                    if (!isOverlapping(newField, pieceToPlace, i, j)){
                    permutations.add(addPiece(newField, pieceToPlace, pentID, i, j));
										//System.out.println(permutations.size());
                    }
                }
            }
				            possibilities.addAll(permutations);
        }
        return possibilities;
    }

    /// nigward: returns a new version of the board initField, with a new pentomino piece at (x, y).
    /// it doesn't actually check if the move is legal, this is done by calling isOverlapping() beforehand lol
    public static int[][] addPiece(int[][] initField, int[][] pieceToPlace, int pentID, int x, int y){
      int[][] newField = initField;
      for (int i = 0; i < pieceToPlace.length; i++){
        for (int j = 0; j < pieceToPlace[0].length; j++){
           if (pieceToPlace[i][j] == 1)
              newField[x+i][y+j] = pentID;
        }
      }
      return newField;
    }

    /// nigward: returns true if placing pieceToPlace at (x,y) would overlap existing pieces in initField.
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

    /*
    heuristic function returning the minimum area of open spaces
    returns gridArea+1 when it's a solution
    returns gridArea when it's empty
    otherwise it returns the smallest area of empty spaces
    */
    /// nigward: the point of this function is optimization (which is why it's a heuristic).
    /// It gives you the smallest hole left in the board to fill, without actually trying to fit in any shape.
    /// This allows us to check if the smallest hole left is a mulitple of 5, because otherwise we can't fill all space with a pentomino.
    /// We can use this to filter out possible moves without even trying all pentomino placements.
    /// Take note that `amountsReplaced` is static and is modified in both
    /// `minEnclosingArea()` and in `recursiveEmptyAreaSearch()`, the impl is pretty retarded
    static int amountsReplaced = 0;
    public static int minEnclosingArea(int[][] field){
        /// nigward: We start off with w*h+1 as the return value, which is impossible obviously (w*h is the max).
        /// This is used because the line with Math.min() will replace this value as soon as an area is found, since any found area will always be smaller,
        /// AND because if this value - an impossibly large area - is returned, we can use that to signal that no space was found - which means the board is fully solved.
        int minEnclosingArea = (horizontalGridSize*verticalGridSize)+1;
        int numEmptyPlaces = 0;
        for(int[] row : field){
            for(int place : row){
                if(place == -1){
                    numEmptyPlaces += 1;
                }
            }
        }
        //copy the grid
        int[][] searchField = new int[field.length][field[0].length];
        for(int i=0; i<field.length; i++){
            for(int j=0; j<field[0].length; j++){
                searchField[i][j] = field[i][j];
            }
        }
        //search for any minus one until everything changed to -2
        while(amountsReplaced != numEmptyPlaces){
            for(int i=0; i<searchField.length; i++){
                for(int j=0; j<searchField[0].length; j++){
                    if(searchField[i][j] == -1){
                        int currentArea = recursiveEmptyAreaSearch(searchField, i, j);
                        //divisibility check on every area
                        //if(!(currentArea % 5 == 0)){
                          //return 1;
                        //}

                        /// nigward: Use the new result if it's smaller than our old one, which allows us to find the minimum of all results.
                        minEnclosingArea = Math.min(minEnclosingArea, currentArea);
                    }
                }
            }
        }
        amountsReplaced = 0;
        return minEnclosingArea;

    }

    /// nigward: this basically finds the size of the empty area at (x, y) using the flood fill algorithm.
    /// look up `flood fill algorithm` on google images or youtube if you don't get it it's pretty easy
    private static int recursiveEmptyAreaSearch(int[][] searchField, int x, int y){
        int counter = 1;
        if(searchField[x][y] == -1){
            searchField[x][y] = -2;
            amountsReplaced++;
        }
        if(x != 0){
            if(searchField[x-1][y] == -1){
                counter += recursiveEmptyAreaSearch(searchField, x-1, y);
            }
        }
        if (y != 0){
            if(searchField[x][y-1] == -1){
                counter += recursiveEmptyAreaSearch(searchField, x, y-1);
            }
        }
        if(y != searchField[0].length-1){
            if(searchField[x][y+1] == -1){
                counter += recursiveEmptyAreaSearch(searchField, x, y+1);
            }
        }
        if(x != searchField.length-1){
            if(searchField[x+1][y] == -1){
                counter += recursiveEmptyAreaSearch(searchField, x+1, y);
            }
        }
        return counter;
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

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();

        boolean solution = heuristicDepthFirstSearch();

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

				if(!solution){
            System.out.println("No solution possible.");
        }
        else{
            System.out.println("Solution found!");
            System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000+" ms");
        }
    }
}
