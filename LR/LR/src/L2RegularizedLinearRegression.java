import Jama.Matrix;
import model.Question;

/**
 *
 * L2 Regularized Linear Regression
 *
 * Created by jalpanranderi on 2/3/15.
 */
public class L2RegularizedLinearRegression {
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
     * @param input Matrix X and Matrix Y as Quesiton input
     * @param lambda Integer representing lambda as penalty
     * @return Matrix representing the weights
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
