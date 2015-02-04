import Jama.Matrix;
import conts.FileConts;
import model.Question;
import utils.FileUtils;

import java.io.*;

/**
 * Generate Ein and Eout error for the given data set
 * for L2 Regularized Linear Regression
 *
 * Created by jalpanranderi on 1/25/15.
 */
public class Question1 {


    public static void main(String[] args) throws IOException {

        //TODO: enable command line input

        String output = performLinearRegression(FileConts.FILE_1, FileConts.TEST_1);
        FileUtils.writeFile(output, "output_100-10.csv");

        output = performLinearRegression(FileConts.FILE_2, FileConts.TEST_2);
        FileUtils.writeFile(output, "output_100-100.csv");


        output = performLinearRegression(FileConts.FILE_3, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_1000-10.csv");

        output = performLinearRegression(FileConts.FILE_4, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_50(1000)-100.csv");

        output = performLinearRegression(FileConts.FILE_5, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_100(1000)-100.csv");

        output = performLinearRegression(FileConts.FILE_6, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_150(1000)-100.csv");


    }


    /**
     * performLinearRegression learns from the input file and return the test result specified the
     * test in the given test file
     * @param input_file String representing path of input learning data
     * @param test_file String representing path of the test data
     * @return String representing the test results
     * @throws IOException
     */
    private static String performLinearRegression(String input_file, String test_file) throws IOException {
        Question input = FileUtils.read_file(input_file);
        Question test = FileUtils.read_file(test_file);


        StringBuilder builder = new StringBuilder();
        for(int lambda = 0; lambda <= 150; lambda++) {
            Matrix w = L2RegularizedLinearRegression.calculate_Weights(input, lambda);

            double E_in = L2RegularizedLinearRegression.getMeanSquareError(input.mat_x, input.mat_y, w);
            double E_out = L2RegularizedLinearRegression.getMeanSquareError(test.mat_x, test.mat_y, w);

            System.out.printf("%.4f \t %.4f\n", E_in, E_out);

            builder.append(String.format("%.4f, %.4f\n",E_in,E_out));
        }

        return builder.toString();
    }



}
