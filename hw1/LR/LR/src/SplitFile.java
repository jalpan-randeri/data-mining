import Jama.Matrix;
import utils.FileUtils;

import java.io.IOException;

/**
 * Created by jalpanranderi on 2/4/15.
 */
public class SplitFile {
    public static void main(String[] args) {
//        if(args.length != 2){
//            System.out.println("Usage SplitFile <input_file.csv> <output dir>");
//            System.exit(-1);
//        }else{
//            try {
//                FileUtils.split_file(args[0], args[1]);
//            } catch (IOException e) {
//                System.err.println(e.getMessage());
//                System.exit(-1);
//            }
//        }


        Matrix m = new Matrix(3, 2);
        print(m);

        Matrix m_transpose = m.transpose();
        print(m_transpose);

        Matrix ans = m_transpose.times(m);
        print(ans);

    }

    private static void print(Matrix m) {
        m.print(10,4);
    }


}
