package utils;

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
        int cols = reader.readLine().split(",").length - 1;  // -1 because last is y
        int rows = FileUtils.getTotalLines(path) - 1;        // -1 beaus first line is labels

        question.mat_x = new double[rows][cols];
        question.mat_y = new double[rows][1];

        int index = 0;
        while((line = reader.readLine()) != null){
            fill_matrix(question.mat_x, question.mat_y, line, index, cols);
            index++;
        }


        return question;
    }

    private static void fill_matrix(double[][] mat_x, double[][] mat_y, String line, int index, int cols_length) {
        String[] ele = line.split(",");
        for(int i = 0; i < cols_length; i++){
            mat_x[index][i] = Float.parseFloat(ele[i]);
        }

        mat_y[index][0] = Double.parseDouble(ele[cols_length]);
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
}
