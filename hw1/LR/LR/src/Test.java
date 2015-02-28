import Jama.Matrix;

/**
 * Created by jalpanranderi on 2/19/15.
 */
public class Test {
    public static void main(String[] args) {

        double[][] a = {{2.7},{2.7},{-2.9},{-1.8}};

        double[][] x = {{1,1,0.8,1}};

        Matrix alpha = Matrix.constructWithCopy(a);
        Matrix mat_x = Matrix.constructWithCopy(x);

        Matrix ans = alpha.transpose().times(mat_x.transpose());
        ans.print(10,5);


    }
}
