import Jama.Matrix;
import conts.FileConts;
import model.InputTestSet;
import model.Question;
import utils.FileUtils;

import java.io.IOException;

/**
 * Created by jalpanranderi on 1/30/15.
 */
public class CrossValidationLinearRegressing {

    public static void main(String[] args) throws IOException {
        int lambda = getLambdaUsingCrossValidation(FileConts.FILE_3);

        System.out.println("Best Lambda is "+lambda);
    }


    /**
     * Find the best Lambda Value for the given dataset
     *
     * @param file_path String representing the file_path
     * @return Integer representing the lambda value
     * @throws IOException
     */
    public static int getLambdaUsingCrossValidation(String file_path) throws IOException {
        Question input = FileUtils.read_file(file_path);
        int k = 10;
        InputTestSet set[] = partiation_set(input, k);


        int best_lambda = 0;

        double min_error_rate = Double.MAX_VALUE;

        for(int lambda = 0; lambda <= 150; lambda++) {

            double error = 0;
            for (int i = 0; i < set.length; i++) {
                error = error + getError(set[i], lambda);
            }

            error = error / set.length;

            System.out.println("Error is "+error);

            if(error < min_error_rate){
                min_error_rate = error;
                best_lambda = lambda;
            }
        }


        return  best_lambda;
    }

    /**
     * Determine Mean Square Error
     *
     * @param inputTestSet Set Representing the inputset
     * @param lambda Integer Representing the lambda
     * @return Error in input set for the given lambda
     */
    private static double getError(InputTestSet inputTestSet, int lambda) {
        Matrix w = learn(inputTestSet.input.mat_x, inputTestSet.input.mat_y, lambda);
        return determineError(inputTestSet.test.mat_x, w, inputTestSet.test.mat_y);
    }

    /**
     * determine the error
     *
     * @param mat_x Matrix X
     * @param w Matrix W
     * @param mat_y Matrix Y
     * @return Double representing error
     */
    private static double determineError(Matrix mat_x, Matrix w, Matrix mat_y) {
        return LinearRegressionL2.getMeanSquareError(mat_x, mat_y, w);
    }

    /**
     * Apple Linear Regression to learn and predict
     * @param mat_x Matrix X
     * @param mat_y Matrix Y
     * @param lambda Integer representing Lambda
     * @return Matrix of Weights
     */
    private static Matrix learn(Matrix mat_x, Matrix mat_y, int lambda) {
        Question input = new Question();
        input.mat_x = mat_x;
        input.mat_y = mat_y;

        return LinearRegressionL2.calculate_Weights(input, lambda);
    }

    /**
     * partition the given input into k sets
     * @param input Question representing input Matrix X,Y
     * @param k Integer representing the number of sets
     * @return InputTestSet[] representing sets
     */
    private static InputTestSet[] partiation_set(Question input, int k) {

        double[][] mat_x = input.mat_x.getArray();
        double[][] mat_y = input.mat_y.getArray();

        int size = mat_x.length / k;

        double[][][] set_mat_x = new double[k][size][mat_x[0].length];
        double[][][] set_mat_y = new double[k][size][1];

        for(int i = 0; i < k; i++){
            set_mat_x[i] = partitionMatrix(mat_x, i, size);
            set_mat_y[i] = partitionMatrix(mat_y, i, size);
        }


        return generateCrossValidateionSet(set_mat_x, set_mat_y);
    }

    /**
     * partition matrix into K size
     * @param matrix Matrix representing input matrix
     * @param index
     * @param length
     * @return
     */
    private static double[][] partitionMatrix(double[][] matrix, int index, int length) {
        double[][] ans = new double[length][matrix[0].length];

        for(int i = 0, j = index * length; i < ans.length; i++, j++){
            ans[i] = matrix[j];
        }

        return ans;
    }

    /**
     * Generate individual Set given Matrix X and Y
     * @param set_mat_x Double 3D array representing the matrix X
     * @param set_mat_y Double 3D array representing the matrix Y
     * @return InputTestSet[] representing the sets
     */
    private static InputTestSet[] generateCrossValidateionSet(double[][][] set_mat_x, double[][][] set_mat_y) {
        InputTestSet[] set = new InputTestSet[set_mat_x.length];

        for(int i = 0; i < set.length; i++){
            set[i] = assignSets(set_mat_x, set_mat_y, i);
        }

        return set;
    }


    /**
     * generate single set from the raw 3D array
     * @param set_mat_x 3D array representing matrix X
     * @param set_mat_y 3D array representing matrix Y
     * @param index Integer representing the current set index
     * @return InputTestSet
     */
    private static InputTestSet assignSets(double[][][] set_mat_x, double[][][] set_mat_y, int index) {
        InputTestSet inputSet = new InputTestSet();
        inputSet.input = combineSetExpect(set_mat_x, set_mat_y, index);
        inputSet.test = getTestSet(set_mat_x, set_mat_y, index);

        return inputSet;
    }


    /**
     * Combine the set of X for input excludes the test set which location is expect
     * @param set_mat_x 3D array representing the Matrix X
     * @param set_mat_y 3D array representing the Matrix Y
     * @param expect index position which is committed
     * @return Question representing the input set
     */
    private static Question combineSetExpect(double[][][] set_mat_x, double[][][] set_mat_y, int expect) {

        int x_length  = set_mat_x[0][0].length;
        int x_size = (set_mat_x.length - 1) * set_mat_x[0].length;

        double[][] mat_x = new double[x_size][x_length];
        double[][] mat_y = new double[x_size][1];

        int x_y_pointer = 0;

        for(int i = 0; i < set_mat_x.length; i++){
            if(i != expect){
                x_y_pointer = fill_matrix(mat_x, mat_y, set_mat_x[i], set_mat_y[i], x_y_pointer);
            }
        }

        Question learn = new Question();
        learn.mat_x = Matrix.constructWithCopy(mat_x);
        learn.mat_y = Matrix.constructWithCopy(mat_y);

        return learn;
    }


    /**
     * Fill the matrix X and Y with the corresponding data
     * @param mat_x 2D array representing the matrix X
     * @param mat_y 2D array representing the matrix Y
     * @param set_mat_x set of matrix
     * @param set_mat_y set of matrix
     * @param x_y_pointer current pointer in the 2D array matrix X and Y
     * @return Integer representing the current index
     */
    private static int fill_matrix(double[][] mat_x, double[][] mat_y, double[][] set_mat_x, double[][] set_mat_y, int x_y_pointer) {
        for(int i = 0; i < set_mat_x.length; i++, x_y_pointer++){
            mat_x[x_y_pointer] = set_mat_x[i];
            mat_y[x_y_pointer] = set_mat_y[i];
        }

        return x_y_pointer;
    }

    /**
     * generate test set for the index
     * @param set_mat_x 3D array representing the matrix X
     * @param set_mat_y 3D array representing the matrix Y
     * @param index Integer representing the test matrix
     * @return Question contains test Matrix X and Y
     */
    private static Question getTestSet(double[][][] set_mat_x, double[][][] set_mat_y, int index) {
        Question test = new Question();
        test.mat_x = Matrix.constructWithCopy(set_mat_x[index]);
        test.mat_y = Matrix.constructWithCopy(set_mat_y[index]);

        return test;
    }




}
