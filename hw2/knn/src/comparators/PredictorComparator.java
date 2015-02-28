package comparators;

import model.Prediction;

import java.util.Comparator;

/**
 * Created by jalpanranderi on 2/19/15.
 */
public class PredictorComparator implements Comparator<Prediction> {

    @Override
        public int compare(Prediction p1, Prediction p2) {
            double d = p1.mDistance - p2.mDistance;
            if(d == 0){
                return 0;
            }else if(d > 0){
                return 1;
            }else{
                return -1;
            }
        }
}

