import comparators.PredictorComparator;
import model.MinMax;
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
            Question[] input = FileUtils.readFile(args[0], true);
            Question[] test = FileUtils.readFile(args[1], false);

            int[] k = {1,3,5,7,9};

            MinMax[] minMaxes = normalizedData(input);


            String header = "@relation HW1_TEST\n" +
                    "\n" +
                    "@attribute sepal_length real\n" +
                    "@attribute sepal_width real\n" +
                    "@attribute petal_length real\n" +
                    "@attribute petal_width real\n" +
                    "%attribute ExampleID\n" +
                    "\n" +
                    "@data\n";

            String prediction = performKNN(input, test, k, minMaxes);

            FileUtils.writeFile(header+prediction, "prediction.arff");
            System.out.println("Prediction file generated: prediction.arff");

        }else{
            System.out.println("Usage KNN <train_file>  <test_file>");
        }



    }

    /**
     * getArray extract the array from the data set
     * @param data Question[] representing the data set
     * @param index Integer representing the extracted feature
     * @return Double[] array of features from the data set
     */
    private static double[] getArray(Question[] data, int index){
        double[] p = new double[data.length];
        for(int i = 0; i < data.length; i++){
            p [i] = data[i].mDatapoints[index];
        }

        return p;
    }

    /**
     * normalizedData normalized the data set
     * @param input Question[] given data set
     * @return MinMax[] representing the arrays of min and max for given features
     *                  of data set
     */
    private static MinMax[] normalizedData(Question[] input) {

        if(input.length == 1){
            return null;
        }

        double[] p0 = getArray(input, 0);
        double[] p1 = getArray(input, 1);
        double[] p2 = getArray(input, 2);
        double[] p3 = getArray(input, 3);

        MinMax[] mm = { getMinMax(p0),
                        getMinMax(p1),
                        getMinMax(p2),
                        getMinMax(p3) };


        updateData(mm, input);


        return mm;
    }

    /**
     * updateData changes the value of data set to normalized value
     * @param mm MinMax[] representing features minimum and maximum values
     * @param input Question[] given data set
     */
    private static void updateData(MinMax[] mm, Question[] input) {
        for(int i = 0; i < input.length; i++){
            for(int p = 0; p < input[i].mDatapoints.length; p++){
                input[i].mDatapoints[p] = getNormalizedValue(input[i].mDatapoints[p], mm[p].mMin, mm[p].mMax);
            }
        }
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
    private static String getPrediction(Question test, Question[] train, int[] k, MinMax[] mm) {

        Prediction[] distances = new Prediction[train.length];
        for(int i = 0; i < distances.length; i++){
            distances[i] = getEuclideanDistance(train[i].mDatapoints, train[i].mLabel, test.mDatapoints, mm);
        }

        Arrays.sort(distances, new PredictorComparator());

//        System.out.println(Arrays.toString(distances));


        StringBuilder builder = new StringBuilder();
        for (int aK : k) {
            builder.append(determineLabel(distances, aK));
            builder.append(",");
        }
        builder.delete(builder.length()- 1, builder.length());
        return builder.toString();
    }

    /**
     * performKNN will run the K-nearest neighbours algorithm and determine the
     * unknown value from the learning data
     * @param input Question[] set of inputs as training data
     * @param test Question[] set of test as testing data
     * @param k Integer[] array list of k
     * @return String representing the prediction
     */
    private static String performKNN(Question[] input, Question[] test, int[] k, MinMax[] mm) {
        StringBuilder builder = new StringBuilder();


        for (int i = 0; i < test.length; i++){
            String label = getPrediction(test[i], input, k, mm);


            String prediction  = String.format("%1.1f,%1.1f,%1.1f,%1.1f,%s,%s\n", test[i].mDatapoints[0],
                    test[i].mDatapoints[1], test[i].mDatapoints[2], test[i].mDatapoints[3], test[i].mTestLabel, label);

//            System.out.printf(prediction);

            builder.append(prediction);
        }

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
        HashMap<String, Double> tiebreaker = new HashMap<String, Double>();

        for(int i = 0; i < k; i++){
            if(counts.containsKey(distances[i].label)){
                counts.put(distances[i].label, counts.get(distances[i].label) + 1);
                tiebreaker.put(distances[i].label, tiebreaker.get(distances[i].label) + distances[i].mDistance);
            }else{
                counts.put(distances[i].label, 1);
                tiebreaker.put(distances[i].label, distances[i].mDistance);
            }
        }

        return getMax(counts, tiebreaker);
    }

    /**
     * getMax returns the label occurring maximum time
     * @param counts HashMap representing the labels and their counts
     * @param tiebreaker HashMap representing the total distance of each label
     * @return String representing the label
     */

    private static String getMax(HashMap<String, Integer> counts, HashMap<String, Double> tiebreaker) {
        String label = null;
        int count = 0;

        for(String key : counts.keySet()){
            if(counts.get(key) == count && tiebreaker.get(key) < tiebreaker.get(label)){
                label = key;
            }else if(counts.get(key) > count){
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
     * @param mm MinMax[] represents the minimum and maximum points for the
     * @return Prediction representing the distance and predicted label
     */
    private static Prediction getEuclideanDistance(double[] inputPoints, String label, double[] testPoints, MinMax[] mm){
        double ans = 0;
        for(int i = 0; i < inputPoints.length; i++){
            ans = ans + Math.pow(inputPoints[i]- getNormalizedValue(testPoints[i], mm[i].mMin, mm[i].mMax), 2);
        }

        Prediction prediction = new Prediction();
        prediction.mDistance = Math.sqrt(ans);
        prediction.label = label;

        return prediction;
    }

    /**
     * getMinMax determine the minimum and maximum value of from the list
     * @param data double[] array
     * @return MinMax representing the minimum and maximum of the given array
     */
    private static MinMax getMinMax(double[] data){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for(double d : data){
            min = Math.min(d, min);
            max = Math.max(d, max);
        }

        return new MinMax(min, max);
    }

    /**
     * getNormalizedValue calculate the normalized value for x
     * @param x Double representing the sample
     * @param x_min Double representing the minimum value
     * @param x_max Double representing the maximum value
     * @return Double normalized value
     */
    private static double getNormalizedValue(double x, double x_min, double x_max){
        return (x - x_min)/(x_max - x_min);
    }
}
