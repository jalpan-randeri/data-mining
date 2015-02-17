import model.Prediction;
import model.Question;
import utils.FileUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by jalpanranderi on 2/16/15.
 */
public class KNN {
    public static void main(String[] args) throws IOException {

        if(args.length == 3) {
//            System.out.println("Training Set");
            Question[] input = FileUtils.readFile(args[0]);
//            System.out.println("Testing Set");
            Question[] test = FileUtils.readFile(args[1]);
            int[] k = new int[1];
            k[0] = Integer.parseInt(args[2]);
//            k[1] = Integer.parseInt(args[3]);
//            k[2] = Integer.parseInt(args[4]);
//            k[3] = Integer.parseInt(args[5]);
//            k[4] = Integer.parseInt(args[6]);

            performKNN(input, test, k);

        }else{
            System.out.println("Usage KNN <train_file>  <test_file>  <k>");
        }
    }

    private static void performKNN(Question[] input, Question[] test, int[] k) {
        for (Question aTest : test) {
            String prediction = getPrediction(aTest, input, k);
            System.out.printf("%.2f,%.2f,%.2f,%.2f,%s\n", aTest.mDatapoints[0],
                    aTest.mDatapoints[1], aTest.mDatapoints[2], aTest.mDatapoints[3], prediction);

        }
    }

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
