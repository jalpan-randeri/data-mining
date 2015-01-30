import Jama.Matrix;
import model.Question;
import utils.FileUtils;

import java.io.*;

/**
 * Created by jalpanranderi on 1/25/15.
 */
public class LinearRegressionL2 {

    private static final String file1 = "LR/train-100-10.csv";
    private static final String file2 = "LR/train-100-100.csv";
    private static final String file3 = "LR/train-1000-100.csv";
    private static final String file4 = "LR/50(1000)_100_train.csv";
    private static final String file5 = "LR/100(1000)_100_train.csv";
    private static final String file6 = "LR/150(1000)_100_train.csv";


    private static final String test1 = "LR/test-100-10.csv";
    private static final String test2 = "LR/test-100-100.csv";
    private static final String test3 = "LR/test-1000-100.csv";


    public static void main(String[] args) throws IOException {
        String output = learn(file1, test1);
        FileUtils.writeFile(output, "output_100-10.csv");

        output = learn(file2, test2);
        FileUtils.writeFile(output, "output_100-100.csv");


        output = learn(file3, test3);
        FileUtils.writeFile(output, "output_1000-10.csv");

        output = learn(file4, test3);
        FileUtils.writeFile(output, "output_50(1000)-100.csv");

        output = learn(file5, test3);
        FileUtils.writeFile(output, "output_100(1000)-100.csv");

        output = learn(file6, test3);
        FileUtils.writeFile(output, "output_150(1000)-100.csv");


    }






    private static String learn(String input_file, String test_file) throws IOException {
        Question input = FileUtils.read_file(input_file);
        Question test = FileUtils.read_file(test_file);

        int N = input.mat_y.getRowDimension();

        StringBuilder builder = new StringBuilder();

        for(int lambda = 0; lambda <= 150; lambda++) {
            Matrix w = calculate_Weights(input, lambda);

            double E_in = getError(input.mat_x, input.mat_y, w, N);
            double E_out = getError(test.mat_x, test.mat_y, w, N);

            System.out.printf("%.4f \t %.4f\n", E_in, E_out);


            builder.append(String.format("%.4f \t %.4f\n",E_in,E_out));

        }

        return builder.toString();
    }

    private static double getError(Matrix mat_x, Matrix mat_y, Matrix w, int N) {

        Matrix y_calcuate = mat_x.times(w);
        Matrix result = y_calcuate.minus(mat_y);

        result = result.transpose().times(result);
        return result.det()/N;
    }

    private static Matrix calculate_Weights(Question input, int lambda) {
        Matrix x_transpose = input.mat_x.transpose();

        Matrix x_transpose_x = x_transpose.times(input.mat_x);

        Matrix mat_lambda = Matrix.identity(x_transpose_x.getRowDimension(), x_transpose_x.getColumnDimension()).times(lambda);

        Matrix x_transpose_x_plus_lamda = x_transpose_x.plus(mat_lambda);
        Matrix x_transpose_x_plus_lambda_inverse = x_transpose_x_plus_lamda.inverse();

        Matrix x_transpose_y = x_transpose.times(input.mat_y);

        return x_transpose_x_plus_lambda_inverse.times(x_transpose_y);
    }


}
