import Jama.Matrix;
import conts.FileConts;
import model.Question;
import utils.FileUtils;

import java.io.*;

/**
 * Question 1
 *
 * Created by jalpanranderi on 1/25/15.
 */
public class LinearRegressionL2 {




    public static void main(String[] args) throws IOException {
        String output = learn(FileConts.FILE_1, FileConts.TEST_1);
        FileUtils.writeFile(output, "output_100-10.csv");

        output = learn(FileConts.FILE_2, FileConts.TEST_2);
        FileUtils.writeFile(output, "output_100-100.csv");


        output = learn(FileConts.FILE_3, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_1000-10.csv");

        output = learn(FileConts.FILE_4, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_50(1000)-100.csv");

        output = learn(FileConts.FILE_5, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_100(1000)-100.csv");

        output = learn(FileConts.FILE_6, FileConts.TEST_3);
        FileUtils.writeFile(output, "output_150(1000)-100.csv");


    }


    /**
     * learn from the input file and return the test result specified the
     * test in the given test file
     * @param input_file String representing path of input learning data
     * @param test_file String representing path of the test data
     * @return String representing the test results
     * @throws IOException
     */
    private static String learn(String input_file, String test_file) throws IOException {
        Question input = FileUtils.read_file(input_file);
        Question test = FileUtils.read_file(test_file);


        StringBuilder builder = new StringBuilder();
        for(int lambda = 0; lambda <= 150; lambda++) {
            Matrix w = calculate_Weights(input, lambda);

            double E_in = getMeanSquareError(input.mat_x, input.mat_y, w);
            double E_out = getMeanSquareError(test.mat_x, test.mat_y, w);

            System.out.printf("%.4f \t %.4f\n", E_in, E_out);

            builder.append(String.format("%.4f, %.4f\n",E_in,E_out));
        }

        return builder.toString();
    }

    /**
     * returns Mean square error for the given matrix
     * @param mat_x Matrix X
     * @param mat_y Matrix Y
     * @param w Matrix W
     * @return Double representing means square error
     */
    public static double getMeanSquareError(Matrix mat_x, Matrix mat_y, Matrix w) {

        Matrix y_calculate = mat_x.times(w);
        Matrix result = y_calculate.minus(mat_y);

        result = result.transpose().times(result);
        return result.det()/mat_x.getRowDimension();
    }

    /**
     * returns the Weight matrix
     * @param input Matrix X and Matrix Y as Question input
     * @param lambda Integer representing lambda as penalty
     * @return Weight Matrix representing the weights for the given system
     */
    public static Matrix calculate_Weights(Question input, int lambda) {
        Matrix x_transpose = input.mat_x.transpose();

        Matrix x_transpose_x = x_transpose.times(input.mat_x);

        Matrix mat_lambda = Matrix.identity(x_transpose_x.getRowDimension(), x_transpose_x.getColumnDimension()).times(lambda);

        Matrix x_transpose_x_plus_lamda = x_transpose_x.plus(mat_lambda);
        Matrix x_transpose_x_plus_lambda_inverse = x_transpose_x_plus_lamda.inverse();

        Matrix x_transpose_y = x_transpose.times(input.mat_y);

        return x_transpose_x_plus_lambda_inverse.times(x_transpose_y);
    }


}
