import Jama.Matrix;
import model.Question;
import utils.FileUtils;

import java.io.*;

/**
 * Created by jalpanranderi on 1/25/15.
 */
public class LinearRegressionL2 {

    private static final String file1 = "LR/train-100-10.csv";


    public static void main(String[] args) throws IOException {
        Question question = FileUtils.read_file(file1);

        Matrix x = Matrix.constructWithCopy(question.mat_x);
        Matrix y = Matrix.constructWithCopy(question.mat_y);




        Matrix x_transpose = x.transpose();

        Matrix x_dagger = ((x_transpose.times(x)).inverse()).times(x_transpose);

        Matrix w = x_dagger.times(y);

        w.print(10, 5);
    }




}
