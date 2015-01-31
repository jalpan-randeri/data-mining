package utils;

import Jama.Matrix;
import model.Question;

/**
 * Utilities methods for modifiying Questions Object
 *
 * Created by jalpanranderi on 1/30/15.
 */
public class QuestionUtils {

    /**
     * Generate Question (Matrix, Matrix) random of length @size,
     * using the given input question sample
     * @param input Question as input
     * @param size length of the output matrix
     * @return Question object containing matrix of length size randomly chosen
     * k item where k = size
     */
    public static Question generateMatrixOfSize(Question input, int size){
        double[][] mat_x = input.mat_x.getArray();
        double[][] mat_y = input.mat_y.getArray();

        int[] indexes = new int[mat_x.length];
        RandomUtils.fillSeries(indexes);
        RandomUtils.shuffle(indexes);

        double[][] matrix_x = new double[size][mat_x[0].length];
        double[][] matrix_y = new double[size][1];
        for(int i = 0; i < size; i++){
            matrix_x[i] = mat_x[indexes[i]];
            matrix_y[i] = mat_y[indexes[i]];
        }

        Question result = new Question();
        result.mat_x = Matrix.constructWithCopy(matrix_x);
        result.mat_y = Matrix.constructWithCopy(matrix_y);

        return result;
    }
}
