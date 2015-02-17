import Jama.Matrix;
import model.Question;
import utils.FileUtils;
import utils.QuestionUtils;

import java.io.IOException;

/**
 *
 * Question 2
 *
 * L2 Regularized Linear Regression using lambda values
 *
 * Created by jalpanranderi on 1/30/15.
 */
public class Question2 {

    public static void main(String[] args) {


        if(args.length != 4){
            System.out.println("Usage Question2 <lambda> <input.csv> <test.csv> <outfile>");
            System.exit(-1);

        }else{
            int lambda = Integer.parseInt(args[0]);
            String input_file = args[1];
            String test_file = args[2];
            String output_file = args[3];


            try {
                Question test = FileUtils.read_file(test_file);
                Question input = FileUtils.read_file(input_file);


                String output = performLinearRegressingUsingLambda(input, test, lambda);
                FileUtils.writeFile(output, output_file);

            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }



    }


    /**
     * Learn using the lambda and given input data and test it on test data
     * @param input Question representing input Matrix X, Y
     * @param test Question representing the test Matrix X, Y
     * @param lambda Integer representing the penalty term
     * @return String representing the result
     */
    private static String performLinearRegressingUsingLambda(Question input, Question test, int lambda) {
        StringBuilder builder = new StringBuilder();
        builder.append("E-in, E-out\n");

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
