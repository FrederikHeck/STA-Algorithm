/* Student-Topic-Assignment (mainSTA): Find a perfect assignment of students to topics
 * -------------------------------------------------------------------------------
 *
 * USE THE PROGRAM
 *
 * The program can be executed via command line. It expects 3 String-values as input.
 * Later two of them will be converted in integer, so please use only numbers.
 *
 * The values are:
 * * int studentCount: The total number of students, that need to be assigned to a topic
 * * int topicCount: The total number of topics, student's can wish to be assigned to
 * * String csvFile: A path to a CSV-file. In the file all weighted students topic wishes are saved.
 *   The file needs to be specially formatted. Refer to the CSVReader documentation.
 *
 * To use the program via console please use following command:
 * >>>java mainSTA [studentCount] [topicCount] [csvFile]
 *
 * The program will let you know, if something went wrong. Please check the CSV-file format in this case.
 * Also check the order of the arguments you gave. Make sure that the int numbers are correct.
 * Also make sure that you encapsulated the file path like this "[csvFile]" if it contains blanks.
 * -------------------------------------------------------------------------------
 *
 * UNDERSTAND THE PROGRAM
 *
 * Step 1: Read in
 * This program takes an CSV-file in which a student-topic matrix is saved.
 * It also takes the total number of students and topics.
 * The matrix indicates, which student wants to be assigned to which topic.
 * Students may give a priority for each topic. Lower priorities signalise high interest in a topic.
 * Priorities are integer values p, each p >= 1
 *
 * Step 2: Main Work
 * The program processes the data for the main work.
 * The main work is an external 1-to-1-adopted algorithm from a library. Credits are given below.
 * The algorithm takes the preprocessed data and returns an optimal matching.
 * It makes use of graph theory, specifically of network flows and their maximisation.
 *
 * Step 3: Output
 * The optimal matching is presented as console output as well as the total weight-cost, according to the algorithm.
 * The weight-cost number will not be very informative as stand-alone. It may be useful if the extended mainSTA problem
 * wants to be solved.
 *
 * -------------------------------------------------------------------------------
 *
 * POSSIBLE OPTIMIZATIONS:
 *
 * todo: implement a solution for the extended mainSTA-problem
 * todo: improve error handling
 *
 * -------------------------------------------------------------------------------
 *
 * @author: Frederik Heck
 * @credits: A Java Library of Graph Algorithms and Optimization, by H.T. Lau
*/
public class mainSTA {

    /* The program expects 3 inputs:

    */
    static int studentCount;
    static int topicCount;
    static String csvFile;

    //static helper arrays for data processing
    static double[][] assignmentMatrix;
    static int[] solutionArray;

    public static void main(String[] assignmentData) {
        try{
            //STEP 1: Read in

            /*
            // use this for debugging
            studentCount = 37;
            topicCount = 42;
            csvFile = "doc\\example.csv";
            //*/

            //*
            // use this for console-based input
            studentCount = Integer.parseInt(assignmentData[0]);
            topicCount = Integer.parseInt(assignmentData[1]);
            csvFile = assignmentData[2];
            //*/

            makeAssignments(); //STEP 2: Main work
            printAssignments(); //STEP 3: Output

        }catch(Exception e){
            System.out.println("There was an error."
                                + " Please check your input data.\n");
            System.out.println("The program expects this input:");
            System.out.println(">>>[studentCount] [topicCount] [csvFile]\n");
            System.out.println("For further information"
                                + " consult the documentation.");
        }
    }

    public static void makeAssignments(){
        //matrix preparation
        int matrixSize = studentCount + topicCount;
        solutionArray = new int[matrixSize + 1];
        assignmentMatrix = CSVReader.readAssignmentMatrix(csvFile,
                                                    studentCount, topicCount);

        // do matching algorithm
        GraphAlgo.minSumMatching(matrixSize, assignmentMatrix,
                                                        solutionArray);
    }

    //present result of algorithm
    public static void printAssignments(){
        System.out.println("\nOptimal matching:");
        int topic, priority;
        for(int student = 1; student <= studentCount; ++student) {
            topic = solutionArray[student] - studentCount;
            priority = (int)assignmentMatrix[student][solutionArray[student]];
            if(priority < 1000)
                System.out.println(" s" + student
                        + " -- t" +  topic + " [P" + priority + "]");
            else{
                System.out.println(" s" + student
                            + " --  no matching possible");
            }
        }

        System.out.println("\nTotal optimal matching cost = "
                                            + assignmentMatrix[0][0]);

    }
}
