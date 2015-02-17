package model;

/**
 * Question representing the model of the input and test data
 *
 * Created by jalpanranderi on 2/16/15.
 */
public class Question {
    public double[] mDatapoints;
    public String mLabel;

    @Override
    public String toString() {
        return String.format("%.2f,%.2f,%.2f,%.2f,%s",mDatapoints[0],mDatapoints[1],mDatapoints[2],mDatapoints[3], mLabel);
    }



}
