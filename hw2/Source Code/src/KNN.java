import model.Prediction;
import model.Question;
import utils.FileUtils;

import java.io.IOException;
import java.util.*;

/**
 * KNN is K Nearest Neighbour classifier Algorithm Implementation
 *
 * Created by jalpanranderi on 2/16/15.
 */
public class KNN {
    public static void main(String[] args) throws IOException {

        if(args.length == 2) {
            Question[] input = FileUtils.readFile(args[0]);
            Question[] test = FileUtils.readFile(args[1]);
            int[] k = {1,3,5,7,9};

            String prediction = performKNN(input, test, k);
            FileUtils.writeFile(prediction, "prediction.txt");

        }else{
            System.out.println("Usage KNN <train_file>  <test_file>");
        }
    }

    /**
     * perfromKNN will run the K-nearest neighbours algorithm and determine the
     * unknown value from the learning data
     * @param input Question[] set of inputs as training data
     * @param test Question[] set of test as testing data
     * @param k Integer[] array list of k
     * @return String representing the prediction
     */
    private static String performKNN(Question[] input, Question[] test, int[] k) {
        StringBuilder builder = new StringBuilder();


        for (Question aTest : test) {
            String label = getPrediction(aTest, input, k);
//            System.out.printf("%.2f,%.2f,%.2f,%.2f,%s\n", aTest.mDatapoints[0],
//                    aTest.mDatapoints[1], aTest.mDatapoints[2], aTest.mDatapoints[3], label);
            String prediction  = String.format("%.2f,%.2f,%.2f,%.2f,%s\n", aTest.mDatapoints[0],
                    aTest.mDatapoints[1], aTest.mDatapoints[2], aTest.mDatapoints[3], label);
            builder.append(prediction);
        }

        return builder.toString();
    }

    /**
     * getPrediction will predict the class of the given unknown sample from the
     * training samples
     *
     * @param test Question[] sets of Input as training data
     * @param train Question unknown sample from test data
     * @param k Integer[] representing number of neighbours we need to consider
     * @return String representing the prediction class of the given sample
     */
    private static String getPrediction(Question test, Question[] train, int[] k) {

        Prediction[] distances = new Prediction[train.length];
        for(int i = 0; i < distances.length; i++){
            distances[i] = getEuclideanDistance(train[i].mDatapoints, train[i].mLabel, test.mDatapoints);
        }

        Arrays.sort(distances, new Comparator<Prediction>() {
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
        });

        StringBuilder builder = new StringBuilder();
        for (int aK : k) {
            builder.append(determineLabel(distances, aK));
            builder.append(",");
        }
        builder.delete(builder.length()- 1, builder.length());
        return builder.toString();
    }


    /**
     * determineLabel determine the most occurring label from the prediction set
     * @param distances Predication[] representing the distance of the unknown sample to known samples
     * @param k Integer representing the number of neighbours will affect the prediction
     * @return String representing the label of the unknown sample
     */
    private static String determineLabel(Prediction[] distances, int k) {
        HashMap<String, Integer> counts = new HashMap<String, Integer>();

        for(int i = 0; i < k; i++){
            if(counts.containsKey(distances[i].label)){
                counts.put(distances[i].label, counts.get(distances[i].label) + 1);
            }else{
                counts.put(distances[i].label, 1);
            }
        }

        return getMax(counts);
    }

    /**
     * getMax returns the label occurring maximum time
     * @param counts HashMap representing the labels and their counts
     * @return String representing the label
     */

    private static String getMax(HashMap<String, Integer> counts) {
        String label = null;
        int count = 0;
        for(String key : counts.keySet()){
            if(counts.get(key) > count){
                count = counts.get(key);
                label = key;
            }
        }
        return label;
    }


    /**
     * getEuclideanDistance returns the distance between given two points
     * @param inputPoints Double[] representing the sets of input points
     * @param label String representing the label of the input point (known sample from training data)
     * @param testPoints Double[] representing the sets of testpoints
     * @return Prediction representing the distance and predicted label
     */
    private static Prediction getEuclideanDistance(double[] inputPoints, String label, double[] testPoints){
        double ans = 0;
        for(int i = 0; i < inputPoints.length; i++){
            ans = ans + Math.pow(inputPoints[i] - testPoints[i], 2);
        }

        Prediction prediction = new Prediction();
        prediction.mDistance = Math.sqrt(ans);
        prediction.label = label;

        return prediction;
    }
}
