package model;

/**
 *
 * Predication representing the intermediate prediction results.
 *
 * Created by jalpanranderi on 2/16/15.
 */
public class Prediction {
    public double mDistance;
    public String label;

    @Override
    public String toString() {
        return mDistance+"";
    }
}
