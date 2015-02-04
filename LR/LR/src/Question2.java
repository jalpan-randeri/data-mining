import Jama.Matrix;
import conts.FileConts;
import model.Question;
import utils.FileUtils;
import utils.QuestionUtils;

import java.io.IOException;

/**
 *
 * Question 2
 *
 * Created by jalpanranderi on 1/30/15.
 */
public class Question2 {

    public static void main(String[] args) throws IOException {

        learn(FileConts.FILE_3, FileConts.TEST_3);

    }

    /**
     * predict from the input file and test on test file
     *
     * @param input_file String representing the input file
     * @param test_file String representing the test file
     * @throws IOException
     */
    private static void learn(String input_file, String test_file) throws IOException {
        Question input = FileUtils.read_file(input_file);
        Question test = FileUtils.read_file(test_file);



        String output_lambda_1 = learnUsingLambda(input, test, 1);
        FileUtils.writeFile(output_lambda_1, "output_lambda_1.csv");

        String output_lambda_25 = learnUsingLambda(input, test, 25);
        FileUtils.writeFile(output_lambda_25, "output_lambda_25.csv");


        String output_lambda_150 = learnUsingLambda(input, test, 150);
        FileUtils.writeFile(output_lambda_150, "output_lambda_150.csv");

    }

    /**
     * Learn using the lambda and given input data and test it on test data
     * @param input Question representing input Matrix X, Y
     * @param test Question representing the test Matrix X, Y
     * @param lambda Integer representing the penalty term
     * @return String representing the result
     */
    private static String learnUsingLambda(Question input, Question test, int lambda) {
        StringBuilder builder = new StringBuilder();

        for(int i = 20; i < input.mat_x.getRowDimension(); i = i + 20) {
            double final_E_in = 0;
            double final_E_out = 0;

            int iteration_size = 50;
            for(int j = 0; j <  iteration_size; j++) {
                Question random = QuestionUtils.generateMatrixOfSize(input, i);
                Matrix w = L2RegularizedLinearRegression.calculate_Weights(random, lambda);
                double E_in = L2RegularizedLinearRegression.getMeanSquareError(random.mat_x, random.mat_y, w);
                double E_out = L2RegularizedLinearRegression.getMeanSquareError(test.mat_x, test.mat_y, w);

                final_E_in = final_E_in + E_in;
                final_E_out = final_E_out + E_out;
            }

            final_E_in = final_E_in / iteration_size;
            final_E_out = final_E_out / iteration_size;

//            System.out.printf("%.4f \t %.4f\n",final_E_in, final_E_out);
            builder.append(String.format("%.4f, %.4f\n",final_E_in, final_E_out));
        }

        return builder.toString();
    }


}
