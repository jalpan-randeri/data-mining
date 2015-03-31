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


        if(args.length != 3){

            System.out.println("Usage Question1 <input.csv> <test.csv> <outfile>");
            System.exit(-1);

        }else{
            String input_file = args[0];
            String test_file = args[1];
            String output_file = args[2];


            try {
                String output = performLinearRegression(input_file, test_file);
                FileUtils.writeFile(output, output_file);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }



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
        builder.append("E-in, E-out\n");


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
