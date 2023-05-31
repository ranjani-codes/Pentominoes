import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.Random;
import java.text.DecimalFormat;


public class parcels_GA{

    static String file_name = "parcels_file.csv"; //Enter file where the blocs are stored

    //- - - - - - - - - - - - USAGE INSTRUCTIONS- - - - - - - - - - - - - - //
    //HOW TO USE?
    //info section explains the behaviour of the algorithm
    //Using values? -> Section 2
    //Using only A,B,C without values? -> Section 1
    //Using the brute will modify the way the Simulation method places the parcels
    //Using the bruteforce will modify the way the Simulation method places the parcels. advised to keep it 'True'


    // - - - - - Section 1 - - - - -
    //Step 1.0: fill A,B,C number of parcels
    //Step 1.1: set the boolean ValueBoolean = false;
    //Step 1.2: Add a time-limit of execution in milliseconds.
    //Step 1.3: RUN the program

    //info 0.0: Volume total parcels > container volume?
    //  --> Will say you can't use that number of parcels
    //info 1.0: Volume total parcels <= container volume
    //info 1.1: Volume total parcels = container volume and all parcels CAN be placed
    //  --> Will find the container, all filled
    //info 1.2: Volume total parcels < container volume and all parcels CAN be placed
    //  --> Will find the container, with gaps obviously
    //info 1.3: Volume total parcels < container volume and all parcels CAN't be placed
    //  --> Will remove some pieces and try to get the most pieces placed, will stop with the time-limit

    // - - - - - Section 2 - - - - -
    //Step 2.0: fill A,B,C number of parcels, if unlimited write values 60,44,50 respectively
    //Step 2.1: fill A,B,c's respective weights
    //Step 2.2: set the boolean ValueBoolean = true;
    //Step 2.3: Add a time-limit of execution in milliseconds.
    //Step 2.4: RUN the program

    //info 0.0: Volume total parcels > container volume?
    //  --> Will stop after the timeLimit and show the best result
    //info 1.0: Volume total parcels <= container volume
    //info 1.1: Volume total parcels = container volume and all parcels CAN be placed
    //  --> Will find the container, all filled and show the best result
    //info 1.2: Volume total parcels < container volume and all parcels CAN be placed
    //  --> Will find the container, with gaps obviously and show the best result
    //info 1.3: Volume total parcels < container volume and all parcels CAN't be placed
    //  --> Will stop after the timeLimit and show the best result

    // - - - - - FILL IN: - - - - -

    static int timeLimit = 1*1000*60;
    static  boolean ValueBoolean = false;

    static int A = 60;  //2,2,4 = 16 Y 28
    static int B = 44; //2,3,4 = 24 DG 16
    static int C = 50; //3,3,3 = 27 LG 17  ==1291

    static int weight_A = 3;
    static int weight_B = 4;
    static int weight_C = 5;

    static boolean BruteForce = false;

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    static int n_blocs = A + B + C; //Number of types of blocs
    static int [] container_size = {8,33,5}; //Container size y,x,z respectively all x2 {8,33} == 1320

    //GA variables
    static int pop_size = 150; //number of chromosomes between 150-350 advised
    static int elitism = (int)(pop_size*0.1); //number of picked chromosomes that will be transfered to the next gen between 0.1 - 0.01 advised
    static int tournament = 2; //between how many values in the pop 2, divider not too small otherwise almost always the elites gonna be picked
    static double mutation = 0.1; //between 1 - 0.6 advised

    static int[] chromosome = new int[2 * n_blocs]; //Defining the length of the chromosome
    static int current_gen = 0;
    static int[][] population = new int[pop_size][chromosome.length];
    static double[] fitness_scores = new double[population.length];

    //Simulation variables
    static int x = 0;
    static int y = 0;
    static int z = 0;
    static int pieceVal = 0;
    static int blocs_placed = 0;
    static int totalValue = 0;

    //Best values when time-limit
    static int bestValue = 0;
    static int bestBlocPlaced = 0;
    static double bestFitnessPlaced = 0;
    static double bestVolumePlaced = 0;
    static int [][][] bestMatrix = new int[container_size[0]][container_size[1]][container_size[2]];

    //Fitness variables
    static boolean filled = false;
    static int [] pieceV = new int[n_blocs];

    //Other variables
    static ArrayList<ArrayList<Integer>> file = new ArrayList<>();
    static public long startTime;
    static int containerV = container_size[0]*container_size[1]*container_size[2];



    //Makes the number of times a piece occurs into an ArrayList
    public static void Maker() {

        Reader(); //Read the blocs

        int blocsV = 0;

        ArrayList<ArrayList<Integer>> newFile = new ArrayList<>(); //will replace the file ArrayList when every piece has been created

        int lineFile = 0; //actual line in the official file from the Reader();
        int lineNF = 0; //actual line in the newFile
        int pieceNumberNF = 0; //actual piece number in the newFile
        int rotationCounter = 0; //counts the number of rotations of a piece, because it causes problem when reading the csv file. need to advance +rotationCounter times


        //A - copy the number of times it occurs in the file
        int rotationCounterA = rotationCounter;
        for (int i = 0; i < A; i++) {
            for (int j = 0; j < 3; j++) { //j<1 because only 1 rotation
                newFile.add(new ArrayList<>());
                newFile.get(lineNF).addAll(Arrays.asList(pieceNumberNF, j, file.get(lineFile+rotationCounterA).get(2),
                        file.get(lineFile+rotationCounterA).get(3), file.get(lineFile+rotationCounterA).get(4),
                        file.get(lineFile+rotationCounterA).get(5), file.get(lineFile+rotationCounterA).get(6), file.get(lineFile+rotationCounterA).get(7),
                        file.get(lineFile+rotationCounterA).get(8), file.get(lineFile+rotationCounterA).get(9), file.get(lineFile+rotationCounterA).get(10),
                        file.get(lineFile+rotationCounterA).get(11), file.get(lineFile+rotationCounterA).get(12),file.get(lineFile+rotationCounterA).get(13),
                        file.get(lineFile+rotationCounterA).get(14), file.get(lineFile+rotationCounterA).get(15), file.get(lineFile+rotationCounterA).get(16),
                        file.get(lineFile+rotationCounterA).get(17),file.get(lineFile+rotationCounterA).get(18), file.get(lineFile+rotationCounterA).get(19),
                        file.get(lineFile+rotationCounterA).get(20)));
                lineNF++;
                rotationCounterA++;
            }
            rotationCounterA = rotationCounter;
            blocsV += (newFile.get(lineNF-1).get(2) * newFile.get(lineNF-1).get(3)* newFile.get(lineNF-1).get(4));
            pieceNumberNF++;
        }
        lineFile += 3; //3 rotation


        //B - copy the number of times it occurs in the file
        int rotationCounterB = rotationCounter;
        for (int i = 0; i < B; i++) {
            for (int j = 0; j < 6; j++) { //j<2 because 2 rotations
                newFile.add(new ArrayList<>());
                newFile.get(lineNF).addAll(Arrays.asList(pieceNumberNF, j, file.get(lineFile+rotationCounterB).get(2),
                        file.get(lineFile+rotationCounterB).get(3), file.get(lineFile+rotationCounterB).get(4),
                        file.get(lineFile+rotationCounterB).get(5), file.get(lineFile+rotationCounterB).get(6), file.get(lineFile+rotationCounterB).get(7),
                        file.get(lineFile+rotationCounterB).get(8), file.get(lineFile+rotationCounterB).get(9), file.get(lineFile+rotationCounterB).get(10),
                        file.get(lineFile+rotationCounterB).get(11), file.get(lineFile+rotationCounterB).get(12),file.get(lineFile+rotationCounterB).get(13),
                        file.get(lineFile+rotationCounterB).get(14), file.get(lineFile+rotationCounterB).get(15), file.get(lineFile+rotationCounterB).get(16),
                        file.get(lineFile+rotationCounterB).get(17),file.get(lineFile+rotationCounterB).get(18), file.get(lineFile+rotationCounterB).get(19),
                        file.get(lineFile+rotationCounterB).get(20), file.get(lineFile+rotationCounterB).get(21),file.get(lineFile+rotationCounterB).get(22),
                        file.get(lineFile+rotationCounterB).get(23), file.get(lineFile+rotationCounterB).get(24), file.get(lineFile+rotationCounterB).get(25),
                        file.get(lineFile+rotationCounterB).get(26),file.get(lineFile+rotationCounterB).get(27), file.get(lineFile+rotationCounterB).get(28)));
                lineNF++;
                rotationCounterB++;
            }
            rotationCounterB = rotationCounter;
            blocsV += (newFile.get(lineNF-1).get(2) * newFile.get(lineNF-1).get(3)* newFile.get(lineNF-1).get(4));
            pieceNumberNF++;
        }

        lineFile += 6; //6 rotations


        //C - copy the number of times it occurs in the file
        int rotationCounterC = rotationCounter;
        for (int i = 0; i < C; i++) {
            for (int j = 0; j < 1; j++) { //j<1 because only 1 rotation
                newFile.add(new ArrayList<>());
                newFile.get(lineNF).addAll(Arrays.asList(pieceNumberNF, j, file.get(lineFile+rotationCounterC).get(2),
                        file.get(lineFile+rotationCounterC).get(3), file.get(lineFile+rotationCounterC).get(4),
                        file.get(lineFile+rotationCounterC).get(5), file.get(lineFile+rotationCounterC).get(6), file.get(lineFile+rotationCounterC).get(7),
                        file.get(lineFile+rotationCounterC).get(8), file.get(lineFile+rotationCounterC).get(9), file.get(lineFile+rotationCounterC).get(10),
                        file.get(lineFile+rotationCounterC).get(11), file.get(lineFile+rotationCounterC).get(12),file.get(lineFile+rotationCounterC).get(13),
                        file.get(lineFile+rotationCounterC).get(14), file.get(lineFile+rotationCounterC).get(15), file.get(lineFile+rotationCounterC).get(16),
                        file.get(lineFile+rotationCounterC).get(17),file.get(lineFile+rotationCounterC).get(18), file.get(lineFile+rotationCounterC).get(19),
                        file.get(lineFile+rotationCounterC).get(20), file.get(lineFile+rotationCounterC).get(21),file.get(lineFile+rotationCounterC).get(22),
                        file.get(lineFile+rotationCounterC).get(23), file.get(lineFile+rotationCounterC).get(24), file.get(lineFile+rotationCounterC).get(25),
                        file.get(lineFile+rotationCounterC).get(26),file.get(lineFile+rotationCounterC).get(27), file.get(lineFile+rotationCounterC).get(28),
                        file.get(lineFile+rotationCounterC).get(29),file.get(lineFile+rotationCounterC).get(30),file.get(lineFile+rotationCounterC).get(31)));
                lineNF++;
                rotationCounterC++;
            }
            rotationCounterC = rotationCounter;
            blocsV += (newFile.get(lineNF-1).get(2) * newFile.get(lineNF-1).get(3)* newFile.get(lineNF-1).get(4));
            pieceNumberNF++;
        }

        System.out.println("Total volume of the blocs: " + blocsV + "/" + containerV);

        if (blocsV > containerV && ValueBoolean == false){
            System.out.println("Volume too big for the container!");
            System.out.println(blocsV + "/" + containerV);

            System.exit(0);
        }
        file = newFile; //replace old file by the new all piece file
    }

    //Reads the csv file and converts it into an ArrayList
    public static void Reader() {
        File blocs_file = new File(file_name);
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(blocs_file));

            String actual_linePhrase;
            int actual_lineNumber = 0;

            while ((actual_linePhrase = br.readLine()) != null) {

                file.add(new ArrayList<>()); //add new row
                ArrayList<Integer> numbers_beforeC = new ArrayList<>();

                char character = actual_linePhrase.charAt(0);
                int counter = 0;
                int r;
                int counter_list = 0;
                boolean counter_first = true;
                boolean lastNumber = false;

                for (int i = 0; i < actual_linePhrase.length(); i = i + r) {
                    //read all numbers_beforeC before the ,
                    while (character != ',') {
                        counter++;

                        if (counter >= actual_linePhrase.length()) {
                            lastNumber = true;
                            break; //exit the method
                        }

                        numbers_beforeC.add(Character.getNumericValue(character));
                        character = actual_linePhrase.charAt(counter);
                    }
                    if (lastNumber == true) {
                        numbers_beforeC.add(Character.getNumericValue(character));

                    }

                    if (counter_first == false) {
                        numbers_beforeC.remove(0);
                    }

                    if (counter + 1 <= actual_linePhrase.length()) {
                        character = actual_linePhrase.charAt(counter + 1);

                    }

                    //from arraylist to int
                    int number = 0;
                    for (int j = 0; j < numbers_beforeC.size(); j++) {
                        number = number * 10 + numbers_beforeC.get(j);
                    }

                    numbers_beforeC.clear();

                    r = Integer.toString(number).length() + 1;

                    //add the value in the array
                    file.get(actual_lineNumber).add(counter_list, number);
                    //System.out.println(Arrays.toString(file[actual_lineNumber]));
                    counter_list++;
                    counter_first = false;
                }

                actual_lineNumber++;
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Will fill the DNA of the chromosomes in the population randomly
    public static void FirstFill(int[][] population) {

        for (int p = 0; p < pop_size; p++) {
            int[] total_array = new int[population[0].length];

            //random shuffle between the box numbers
            int[] array1 = new int[n_blocs];
            for (int i = 0; i < n_blocs; i++) {
                array1[i] = i;
            }
            //shuffle
            Random rand1 = new Random();
            for (int i = 0; i < array1.length; i++) {
                int randomIndexToSwap = rand1.nextInt(array1.length);
                int temp = array1[randomIndexToSwap];
                array1[randomIndexToSwap] = array1[i];
                array1[i] = temp;
            }


            //Fill in rotations
            int[] array2 = new int[n_blocs];

            for (int a = 0; a < array1.length; a++){
                int numb = array1[a];

                //A rotations
                if (numb < A){
                    double number = Math.random();
                    if (number < 0.33) {
                        array2[a] = 0;
                    } else if (number >= 0.33 && number < 0.63) {
                        array2[a] = 1;
                    }else {
                        array2[a] = 2;
                    }
                }
                //B rotations
                else if (numb >= A && numb < (A + B)){
                    double number = Math.random();
                    if (number < 0.17) {
                        array2[a] = 0;
                    } else if (number >= 0.17 && number < 0.34) {
                        array2[a] = 1;
                    }
                    else if (number >= 0.34 && number < 0.50) {
                        array2[a] = 2;
                    }
                    else if (number >= 0.50 && number < 0.67) {
                        array2[a] = 3;
                    }
                    else if (number >= 0.67 && number < 0.84) {
                        array2[a] = 4;
                    }else {
                        array2[a] = 5;
                    }
                }
                //C rotations
                else {
                    array2[a] = 0;
                }
            }

            System.arraycopy(array1, 0, total_array, 0, array1.length);
            System.arraycopy(array2, 0, total_array, array1.length, array2.length);

            population[p] = total_array;
            //System.out.println(Arrays.toString(total_array));
        }
    }

    //Simulates the placement of the piece in the matrix
    public static int[][][] Simulation(int[][][] matrix, int[][][] bloc_matrix, int x_temp, int y_temp, int z_temp) {

        //X check
        if ((x_temp + bloc_matrix[0].length > container_size[1])) {
            if (y_temp + 1 + bloc_matrix.length <= container_size[0]) {
                //System.out.println("Rec_down");
                return Simulation(matrix, bloc_matrix, 0, y_temp + 1,z_temp);
            } else if (z_temp + 1 + bloc_matrix[0][0].length <= container_size[2]) {
                return Simulation(matrix, bloc_matrix, 0, 0,z_temp+1);
            }else {
                return matrix;
            }
        }

        //Y check
        else if (y_temp + bloc_matrix.length > container_size[0]){
            if (z_temp + 1 + bloc_matrix[0][0].length <= container_size[2]){
                return Simulation(matrix, bloc_matrix, 0, 0,z_temp + 1);
            }else {
                return matrix;
            }
        }

        //Z check
        else if (z_temp + bloc_matrix[0][0].length > container_size[2]) {
            return matrix;
        }

        //X & Y fit & Z fit
        else if (x_temp + bloc_matrix[0].length <= container_size[1] && y_temp + bloc_matrix.length <= container_size[0]
                && z_temp + bloc_matrix[0][0].length <= container_size[2]) {

            for (int v = 0; v < bloc_matrix.length; v++) {
                for (int h = 0; h < bloc_matrix[0].length; h++) {
                    for (int k = 0; k < bloc_matrix[0][0].length; k++) {
                        if (bloc_matrix[v][h][k] == 0) {
                            continue;
                        }
                        if (matrix[v + y_temp][h + x_temp][k + z_temp] != 0) {
                            return Simulation(matrix, bloc_matrix, x_temp + 1, y_temp,z_temp);
                        }
                    }
                }
            }

            int[][][] temp_matrix = matrix;

            //place the matrix
            for (int v = 0; v < bloc_matrix.length; v++) {
                for (int h = 0; h < bloc_matrix[0].length; h++) {
                    for (int k = 0; k < bloc_matrix[0][0].length; k++) {
                        //matrix pas 0 mais cube oui, continuer
                        if (matrix[v + y_temp][h + x_temp][k + z_temp] != 0 && bloc_matrix[v][h][k] == 0) {
                            continue;
                        }
                        //matrix 0, place piece
                        if (matrix[v + y_temp][h + x_temp][k + z_temp] == 0) {
                            temp_matrix[v + y_temp][h + x_temp][k + z_temp] = bloc_matrix[v][h][k];
                        } else {
                            temp_matrix = matrix;
                            return Simulation(matrix, bloc_matrix, x_temp + 1, y_temp, z_temp);
                        }
                    }
                }
            }
            x = x_temp;
            y = y_temp;
            z = z_temp;

            blocs_placed++;
            totalValue += pieceVal;
            //System.out.println("it fits, x:" + x + ", y:" + y);
            matrix = temp_matrix;
            return matrix;
        }
        // System.out.println("No emplacement for this piece");
        return matrix;
    }

    //Fitness volume calculator
    public static double Fitness_volumeCalc(int[][][] matrix) {
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                for (int k = 0; k < matrix[i][j].length; k++) {
                    if (matrix[i][j][k] != 0) {
                        counter++;
                    }
                }
            }
        }
        int totalVolume = matrix.length * matrix[0].length * matrix[0][0].length;
        return (counter * 1.0) / totalVolume;
    }

    //Will update the fitness_scores of each population
    public static void Fitness(int[][] population) {

        //go trough each chromosome of the population
        for (int p = 0; p < population.length; p++) {

            //make an empty matrix of dimensions of the container
            int[][][] matrix = new int[container_size[0]][container_size[1]][container_size[2]]; //matrix is by default filled with 0's
            int[] chromosome = population[p];

            //reset
            y = 0;
            x = 0;
            z = 0;
            blocs_placed = 0;
            totalValue = 0;

            //System.out.println("Gen: " + current_gen + " Chromosome " + p + " : " + Arrays.toString(chromosome));

            //go trough each DNA of the chromosome
            for (int n = 0; n < n_blocs; n++) {

                int bloc_rotation = chromosome[n + n_blocs]; //rotation of the current bloc in the chromosome
                ArrayList<ArrayList<ArrayList<Integer>>> blocMatrix = new ArrayList<>(); //3D arraylist

                //if weigth are present, give each piece a value so it can be added in the simulation part
                if (ValueBoolean == true) {
                    if (chromosome[n] < A) {
                        pieceVal = weight_A;
                    } else if (chromosome[n] >= A && chromosome[n] < (A + B)) {
                        pieceVal = weight_B;
                    } else {
                        pieceVal = weight_C;
                    }
                }

                //System.out.println("Bloc: " + chromosome[n] + "rotation: " + bloc_rotation);

                //find the good line that matches the rotation and the id
                for (int a = 0; a < file.size(); a++) {
                    if (file.get(a).get(0) == chromosome[n] && file.get(a).get(1) == bloc_rotation) {

                        //get dimensions
                        int[] blocDimensions = new int[3]; //3d

                        for (int r = 0; r < 3; r++) { //3d
                            blocDimensions[r] = file.get(a).get(2+r);
                        }

                        //get the matrix of the bloc
                        int counter = (2 + blocDimensions.length); //2 because first is id and second position rotation

                        ArrayList<ArrayList<ArrayList<Integer>>> list1 = new ArrayList<>(blocDimensions[0]);
                        for (int i = 0; i < blocDimensions[0]; i++)
                        {
                            ArrayList<ArrayList<Integer>> list2 = new ArrayList<>(blocDimensions[1]);
                            for (int j = 0; j < blocDimensions[1]; j++)
                            {
                                ArrayList<Integer> list3 = new ArrayList<>(blocDimensions[2]);
                                for (int k = 0; k < blocDimensions[2]; k++)
                                {
                                    list3.add(file.get(a).get(counter));
                                    counter++;
                                }
                                list2.add(list3);
                            }
                            list1.add(list2);
                        }
                        blocMatrix = list1;

                        //convert arraylist to an array
                        int[][][] bloc_matrix = new int[blocDimensions[0]][blocDimensions[1]][blocDimensions[2]];
                        for (int r = 0; r < blocMatrix.size(); r++) {
                            for (int t = 0; t < blocMatrix.get(r).size(); t++) {
                                for (int h = 0; h < blocMatrix.get(r).get(t).size(); h++){
                                    bloc_matrix[r][t][h] = blocMatrix.get(r).get(t).get(h);
                                }
                            }
                        }
                        //simulate the piece on the matrix
                        matrix = Simulation(matrix, bloc_matrix, x,y,z);

                        if (BruteForce){
                            y = 0;
                            x = 0;
                            z = 0;
                        }
                        break;
                    }
                }
            }

            //calc fitness of the volume placed
            double fitness_volumePlaced = Fitness_volumeCalc(matrix);

            if (fitness_volumePlaced == 1){
                filled = true;
            }

            if (!ValueBoolean){ //meaning optimising volume and pieces placed
                double fitness_blocPlaced = ((blocs_placed * 1.0) / n_blocs);
                fitness_scores[p] = (fitness_blocPlaced + fitness_volumePlaced)/2; //optimise the number of boxes (fitness_blocPlaced + fitness_volumePlaced)/2
            }else if (ValueBoolean){ //meaning optimising onlu the value
                fitness_scores[p] = totalValue;
            }

            //when a better bloc_placed has been found, update all the "best" variables
            if (!ValueBoolean && blocs_placed > bestBlocPlaced){
                //System.out.println(bestBlocPlaced);

                bestBlocPlaced = blocs_placed;
                bestFitnessPlaced = fitness_scores[p];
                bestVolumePlaced = fitness_volumePlaced;
                bestMatrix = matrix;
            }
            //when a better bloc_placed has been found, update all the "best" variables
            else if (ValueBoolean && totalValue > bestValue){
                //System.out.println(bestValue);

                bestValue = totalValue;
                bestBlocPlaced = blocs_placed;
                bestFitnessPlaced = fitness_scores[p];
                bestVolumePlaced = fitness_volumePlaced;
                bestMatrix = matrix;
            }

            //stop if bloc founded
            if (blocs_placed == n_blocs){
                Results(p, fitness_volumePlaced, matrix);
            }
        }
    }

    //Print the results
    public static void Results(int p, double fitness_volumePlaced, int [][][] matrix){
        DecimalFormat fitnessFormatted = new DecimalFormat("#.000");

        if (p == -1){
            System.out.println(" - - -  Gen: " + current_gen);
            System.out.println(" - Best fitness: " + fitnessFormatted.format(bestFitnessPlaced));
            System.out.println(" - Bloc placed:" + bestBlocPlaced + "/" + n_blocs);
            System.out.println(" - Volume placed: " + fitnessFormatted.format(fitness_volumePlaced) + "%");
        }else{
            System.out.println(" - - -  Gen: " + current_gen + ", Chromosome: " + p + " - - - ");
            System.out.println(" - Best fitness: " + fitnessFormatted.format(fitness_scores[p]));
            System.out.println(" - Bloc placed:" + blocs_placed + "/" + n_blocs);
            System.out.println(" - Volume placed: " + fitnessFormatted.format(fitness_volumePlaced) + "%");
        }


        if (filled == true){
            System.out.println(" - No gaps ;)");
        } else if (filled == false){
            System.out.println(" - Some gaps ...");
        }
        if (ValueBoolean){
            System.out.println("Best value: " + bestValue);
        }

        System.out.println(Arrays.deepToString(matrix));

        //print the matrix
        for (int d = 0; d < matrix.length; d++) {
            System.out.println(Arrays.deepToString(matrix[d]));
        }

        //print the number of pieces
        int b0 = 0;
        int b1 = 0;
        int b2 = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                for (int k = 0; k < matrix[i][j].length; k++) {
                    if (matrix[i][j][k] == 9) {
                        b0++;
                    } else if (matrix[i][j][k] == 1) {
                        b1++;
                    } else if (matrix[i][j][k] == 2) {
                        {
                            b2++;
                        }
                    }
                }
            }
        }
        System.out.println("A: " + b0/16 + ", B: " + b1/24 + ", C: " + b2/27);

        long stopTime = System.currentTimeMillis();

        System.out.println((stopTime - startTime) + " milisec.");
        System.exit(0);
    }

    //Perform Selection, Crossover and mutation, update the pop
    public static void Genetic() {

        Fitness(population);
        Sorting();

        //Elitism, take the best results and directly next pop
        for (int i = 0; i < elitism; i++) {
            population[i] = Duplicate_checker(population[population.length - 1 - i]);
        }

        //Tournament with pop-Elitism
        for (int i = elitism; i < population.length; i++) {

            int[] parent_1 = population[Tournament_Sel()];
            int[] parent_2 = population[Tournament_Sel()];

            population[i] = Crossover(parent_1, parent_2);
        }
    }

    //Sorting the whole pop and fitness_scores in ascending order
    public static void Sorting() {

        //sort in ascending order
        double tempF;
        int[] tempP;

        for (int i = 0; i < fitness_scores.length; i++) {
            for (int j = i + 1; j < fitness_scores.length; j++) {
                if (fitness_scores[i] > fitness_scores[j]) {
                    tempF = fitness_scores[i];
                    tempP = population[i];

                    fitness_scores[i] = fitness_scores[j];
                    population[i] = population[j];

                    fitness_scores[j] = tempF;
                    population[j] = tempP;
                }
            }
        }
    }

    //Tournament selection
    public static int Tournament_Sel() {

        double temp_scores[] = new double[tournament];
        int temp_popval[] = new int[tournament];

        for (int i = 0; i < tournament; i++) {
            int n = (int) (Math.random() * population.length);
            temp_scores[i] = fitness_scores[n];
            temp_popval[i] = n;
        }

        //Sorting the 2
        //sort in ascending order
        double tempF;
        int tempP;

        for (int i = 0; i < temp_scores.length; i++) {
            for (int j = i + 1; j < temp_scores.length; j++) {
                if (temp_scores[i] > temp_scores[j]) {
                    tempF = temp_scores[i];
                    tempP = temp_popval[i];

                    temp_scores[i] = temp_scores[j];
                    temp_popval[i] = temp_popval[j];

                    temp_scores[j] = tempF;
                    temp_popval[j] = tempP;
                }
            }
        }
        //return the biggest
        return temp_popval[tournament - 1];
    }

    //Check if there are duplicates in the chromosome
    public static int[] Duplicate_checker(int[] child_1) {

        //note that the "Duplicate_checker" method in kinda mutation too
        //because if there are duplicated it will replace them randomly

        //check for double and missing values and replace them
        int low = 0, high = n_blocs - 1;
        ArrayList<Integer> storemissing = new ArrayList<>();
        ArrayList<Integer> storeplacement = new ArrayList<>();

        boolean[] points_of_range = new boolean[high - low + 1];

        for (int i = 0; i < n_blocs; i++) {
            if (low <= child_1[i] && child_1[i] <= high)
                points_of_range[child_1[i] - low] = true;
        }


        for (int x = 0; x <= high - low; x++) {
            if (points_of_range[x] == false)
                storemissing.add((low + x));
        }

        if (storemissing.size() != 0) {
            for (int i = 0; i < n_blocs; i++) {
                for (int j = i + 1; j < n_blocs; j++) {
                    if (child_1[i] == child_1[j]) {
                        storeplacement.add(i);
                        break;
                    }
                }
            }

            for (int i = 0; i < storemissing.size(); i++) {
                child_1[storeplacement.get(i)] = storemissing.get(i);
            }
            return child_1; //it's kinda a mutation because random fill if values aren't there
        }
        return child_1;
    }

    //Crossover
    public static int[] Crossover(int[] parent_1, int[] parent_2) {

        //cycle crossover (CX), because we can't use twice same value
        int[] child_1 = new int[parent_1.length];

        //System.out.println("Parent 1: " + Arrays.toString(parent_1));
        // System.out.println("Parent 2: " + Arrays.toString(parent_2));

        for (int i = 0; i < parent_1.length; i++) {
            if (i % 2 == 0) {
                child_1[i] = parent_1[i];
            } else {
                child_1[i] = parent_2[i];
            }
        }

        child_1 = Duplicate_checker(child_1);

        if (Arrays.equals(parent_1,parent_2)){
            return Duplicate_checker(Mutation(child_1));
        }

        //Go to mutation?
        double mut = Math.random();
        if (mut <= mutation) {
            return Duplicate_checker(Mutation(child_1));
        }
        return child_1;
    }

    //Mutation
    public static int[] Mutation(int[] child_1) {
        int position1 = (int) (Math.random() * n_blocs);
        int number = (int) (Math.random() * n_blocs);

        child_1[position1] = number;

        int position2 = (int) (Math.random() * n_blocs); //pos of the mutation
        int rot = 0;
        double rand = Math.random();

        //A rotations
        if (number < A){
            if (rand < 0.33) {
                rot = 0;
            } else if (rand >= 0.33 && rand < 0.63) {
                rot = 1;
            }else {
                rot = 2;
            }
        }

        //B rotations
        else if (number >= A && number < (A + B)){
            if (number < 0.17) {
                rot = 0;
            } else if (rand >= 0.17 && rand < 0.34) {
                rot = 1;
            }
            else if (rand >= 0.34 && rand < 0.50) {
                rot = 2;
            }
            else if (rand >= 0.50 && rand < 0.67) {
                rot = 3;
            }
            else if (rand >= 0.67 && rand < 0.84) {
                rot = 4;
            }else {
                rot = 5;
            }
        }
        //C rotations
        else {
            rot = 0;
        }

        child_1[position2 + n_blocs] = rot; //because some pieces have more rotations than others

        return child_1;
    }

    //Main
    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        Maker();
        FirstFill(population);

        while (true) {
            Genetic();

            //Display current Gen
            if (current_gen % 100 == 0) {
                System.out.println("Gen: " + current_gen + ", fittest: " + fitness_scores[pop_size-1]);
            }

            //Timelimit
            long stopTime = System.currentTimeMillis();
            if (stopTime - startTime > timeLimit){
                Results(-1, bestVolumePlaced, bestMatrix);
            }
            //System.out.println(Arrays.toString(fitness_scores));
            //System.out.println(Arrays.deepToString(population));
            current_gen++;
        }
    }
}
