package utils;

import Jama.Matrix;
import model.Question;

import java.io.*;

/**
 * Created by jalpanranderi on 1/25/15.
 */
public class FileUtils {


    public static Question read_file(String path) throws IOException {

        Question question = new Question();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String line;
        int cols = reader.readLine().split(",").length;
        int rows = FileUtils.getTotalLines(path) - 1;        // -1 because first line is labels

        double[][] mat_x = new double[rows][cols];
        double[][] mat_y = new double[rows][1];

        int index = 0;
        while((line = reader.readLine()) != null){
            fill_matrix(mat_x, mat_y, line, index, cols);
            index++;
        }

        reader.close();

        question.mat_x = Matrix.constructWithCopy(mat_x);
        question.mat_y = Matrix.constructWithCopy(mat_y);

        return question;
    }

    private static void fill_matrix(double[][] mat_x, double[][] mat_y, String line, int index, int cols_length) {
        String[] ele = line.split(",");
        mat_x[index][0] = 1;
        for(int i = 1; i < cols_length; i++){
            mat_x[index][i] = Double.parseDouble(ele[i - 1]);
        }

        mat_y[index][0] = Double.parseDouble(ele[cols_length - 1]);
    }

    private static int getTotalLines(String path) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new FileReader(new File(path)));
        lnr.skip(Long.MAX_VALUE);
        //Add 1 because line index starts at 0
        int count = lnr.getLineNumber() + 1;
        // Finally, the LineNumberReader object should be closed to prevent resource leak
        lnr.close();

        return count;
    }


    public static void writeFile(String line, String file_name) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file_name);

        writer.print(line);

        writer.close();

    }

    /**
     * split files into 3 different files
     * @param input
     * @throws IOException
     */
    public static void split_file(String input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line = null;
        int count = 0;
        PrintWriter writer50 = new PrintWriter("50(1000)_100_train.csv");
        PrintWriter writer100 = new PrintWriter("100(1000)_100_train.csv");
        PrintWriter writer150 = new PrintWriter("150(1000)_100_train.csv");

        while((line = reader.readLine()) != null && count <= 150){
            if(count < 50){
                writer50.println(line);
            }

            if(count < 100){
                writer100.println(line);
            }

            writer150.println(line);

            count++;
        }

        writer50.close();
        writer100.close();
        writer150.close();

        reader.close();
    }

}
