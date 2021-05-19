import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    This class will read an csv-file with student-topic-assignment priorities,
    convert it in a java matrix and return it.
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    The input csv-file should look like this:

            T1  T2  T3  ..  ..  TN
        S1      1   2
        S2  1
        S3      3
        ..
        ..
        SN

     - Every Row is a student (S1 - SN)
     - Every Column is a topic (T1 - TN)
     - The first column and the first row store indices
     --> These are headers and will be ignored, their content may be anything

     In the table itself the priorities of topic wishes given by the students
     are stored. We call them weights.
     Empty content should be real empty. Don't fill in something else, like "0".

     +++++

     The reader will convert the csv-file into a java-matrix of this form

                ||  S1 - SN  ||  T1 - TN  ||
        ------------------------------------
        S1 - SN ||           ||     X     ||
        ------------------------------------
        T1 - TN ||     Y     ||           ||
        ------------------------------------

        Rows and columns are exactly the same - they both store student
        S1 - student SN, followed by topic T1 - Topic TN.
        The important information (that comes from the csv) is stored in X.

        Why this special structure?
        A quadratic matrix structure is required for the Graph-Algorithm.
        That is because the algorithm is written for problems where every node
        may be assigned to every other node. However in our problem student
        nodes may only be assigned to topic nodes.
        In Y one may store hypothetically the same information as in X,
        however the algorithm will only use data
        from the upper right diagonal of a matrix.
     */

public class CSVReader {

    // Pass the csv file-source and an empty array
    public static double[][] readAssignmentMatrix(String csvFile,
                                        int studentCount, int topicCount) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        // +1 because first row and column are empty
        int matrixSize = studentCount + topicCount + 1;
        double[][] assignmentMatrix = new double[matrixSize][matrixSize];

        // Initialize all elements high weight, so they won't be matched.
        // Later some elements will be overwritten.
        double highWeight = 1000000.0;
        for (int i = 1; i < assignmentMatrix.length; i++) {
            for (int j = 1; j < assignmentMatrix[i].length; j++)
                assignmentMatrix[i][j] = highWeight;
        }

        //read the csv-file line by line
        try {

            br = new BufferedReader(new FileReader(csvFile));
            int lineNum = 0;
            while ((line = br.readLine()) != null) {

                if (lineNum == 0) { // skip header
                    //lineNum as index below will start with 1,
                    //thats as wished because first row is empty
                    lineNum++;
                    continue;
                }

                if (lineNum > 37) break;

                // store all values of the csv in an array,
                // use semicolon as separator
                String[] row = line.split(cvsSplitBy);

                // fill in a row in assignmentMatrix by taking the content
                // of the row-array. In the csv-file should never be more values
                // per line of interest then topics exist. Note that the first
                // value in the assignmentMatrix has to be empty (=> i = 1)
                for (int i = 1; i <= topicCount; i++) {
                    if(i < row.length && !row[i].equals("")){
                        // only do something if the array has
                        // still values that are not empty

                        // fill in the assignmentMatrix:
                        assignmentMatrix[lineNum][studentCount + i]
                                            = Double.parseDouble(row[i]+".0");
                    }
                }
                lineNum++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return assignmentMatrix;
    }

}
