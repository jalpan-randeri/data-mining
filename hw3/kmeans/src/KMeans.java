
import model.Dataset;
import model.Instance;
import utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Input : K
 * <p/>
 * Step 1: choose k random Instances from the input set
 * Step 2: for each Instance : Data set D
 * assign the Ci for which the dist(ci, Instance) is minimum
 * <p/>
 * Step 3: for each Cluster Ci : C
 * calculate mean Instance, and assign it to Ci
 * Step 4: Check if newCi === C for all Ci in C
 * Step 5: if true break and return the clusters else goto step 2,
 * <p/>
 * <p/>
 * Created by jalpanranderi on 3/4/15.
 */
public class KMeans {
    private static final int MAX_ITERATION = 50;
    private static final int MAX_KMEAN_ITERATION = 25;


    public static void main(String[] args) throws IOException {


        Dataset dataset = FileUtils.readFile("input/segment.arff");
        int[] random = FileUtils.readRandomFile("input/dataset.txt");
        int k;

        preprocess(dataset);
        for (k = 1; k <= 12; k++) {


            double[] calculated_sse = new double[MAX_KMEAN_ITERATION];
            double sse = 0;

            int i;
            int start = 0;
            // randomly choose initial centroids
            for (int itr = 0; itr < MAX_KMEAN_ITERATION; itr++) {
                List<Instance> centroids = new ArrayList<Instance>();

                // get random centroids
                for (i = start; i < random.length && i - start < k; i++) {
                    centroids.add(dataset.getPoint(i));
                }
                start = i;

                // calculate the clusters for the chosen centroids
                HashMap<Instance, List<Instance>> clusters = findClusters(dataset, centroids);
                calculated_sse[itr] = calculateSSE(clusters);
                sse = sse + calculated_sse[itr];
            }

            double mean_sse = sse / MAX_KMEAN_ITERATION;
            double std_sse = getStandardDeviation(calculated_sse, mean_sse);
            double min_confidence = mean_sse - 2 * std_sse;
            double max_confidence = mean_sse + 2 * std_sse;
            System.out.printf("%2d\t %.3f\t %.3f\t %.3f\t %.3f\n", k, mean_sse, std_sse, min_confidence, max_confidence);
        }
    }

    /**
     * calculate sum square error
     *
     * @param clusters HashMap
     * @return double as error
     */
    private static double calculateSSE(HashMap<Instance, List<Instance>> clusters) {
        double error = 0;
        for (Instance centroid : clusters.keySet()) {
            double si = 0;
            for (Instance i : clusters.get(centroid)) {
                si = si + subtract(i, centroid);
            }

            error = error + si;
        }

        return error;
    }

    /**
     * subtract the two instance
     *
     * @param x    Instance
     * @param mean Instance
     * @return double
     */
    private static double subtract(Instance x, Instance mean) {
        double temp = 0;
        for (int i = 0; i < x.attribute.length; i++) {
            temp = temp + Math.pow(x.attribute[i] - mean.attribute[i], 2);
        }

        return temp;
    }

    /**
     * pre process dataset to normalize it using z-score
     *
     * @param dataset Dataset
     */
    private static void preprocess(Dataset dataset) {
        Instance[] data = dataset.getPoints();
        double[][] cols = new double[data[0].attribute.length][data.length];

        for (int i = 0; i < cols.length; i++) {
            cols[i] = extractColumn(i, data);
            normalizeColumn(cols[i]);
        }

        updateDataset(dataset, cols);

    }

    /**
     * update dataset with normalized value
     *
     * @param dataset Data set
     * @param cols    Normalized data set
     */
    private static void updateDataset(Dataset dataset, double[][] cols) {
        Instance[] ip = dataset.getPoints();
        for (int i = 0; i < cols.length; i++) {
            for (int j = 0; j < ip.length; j++) {
                double temp = cols[i][j];
                ip[j].attribute[i] = temp;
            }
        }
    }

    /**
     * extract column will extract the column for the given index
     *
     * @param index   Integer as index
     * @param dataset Data set
     * @return Double[]
     */
    private static double[] extractColumn(int index, Instance[] dataset) {
        double[] d = new double[dataset.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = dataset[i].attribute[index];
        }

        return d;
    }

    /**
     * normalize the entire column
     *
     * @param data Double[] data
     */
    private static void normalizeColumn(double[] data) {
        double avg = getAverage(data);
        double std = getStandardDeviation(data, avg);

        for (int i = 0; i < data.length; i++) {
            data[i] = getNormalizedValue(data[i], avg, std);
        }
    }

    /**
     * find clusters calculate the clusters for the given centroids
     *
     * @param dataset   List<Instance>
     * @param centroids List<Instance>
     * @return HashMap<Instance, List<List<Instance>> representing the list of clusters
     */
    private static HashMap<Instance, List<Instance>> findClusters(Dataset dataset, List<Instance> centroids) {


        List<Instance> calculateCentroids = new ArrayList<Instance>();
        HashMap<Instance, List<Instance>> clusters = new HashMap<Instance, List<Instance>>();

        int count = 0;
        while (!isCentroidSame(calculateCentroids, centroids) && count < MAX_ITERATION) {

            // update only if centroids are already calculated
            if (calculateCentroids.size() != 0) {
                centroids = calculateCentroids;
            }


            clusters = assignInstancesToClusters(centroids, dataset.getPoints());
            calculateCentroids = calculateCentroids(clusters.values());
            count++;
        }

//        printClusters(clusters);

        return clusters;
    }

    /**
     * calculate centroids
     *
     * @param clusters Collection<Instance, <Instance>> determine the centroid for the given each cluster
     * @return List<Instance> list of centroids for the given list of clusters
     */
    private static List<Instance> calculateCentroids(Collection<List<Instance>> clusters) {
        ArrayList<Instance> centroids = new ArrayList<Instance>();

        for (List<Instance> cluster : clusters) {
            Instance centroid = calculateMean(cluster);
            centroids.add(centroid);
        }

        return centroids;
    }


    /**
     * return true iff the both list are same
     *
     * @param calculated List<Instance> list of centroids
     * @param old        List<Instance> list of centroids
     * @return Boolean
     */
    private static boolean isCentroidSame(List<Instance> calculated, List<Instance> old) {
        if (calculated.size() == 0) {
            return false;
        } else {

            boolean ans = false;
            for (Instance c : calculated) {
                ans = false;
                for (Instance o : old) {
                    ans = ans || o.equals(c);
                }
                if (!ans) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * calculate mean for the given list
     *
     * @param cluster List<Instance>
     * @return Instance representing the average value for x and y
     */
    private static Instance calculateMean(List<Instance> cluster) {

        double[] sum = null;
        for (Instance p : cluster) {
            if (sum == null) {
                sum = new double[p.attribute.length];
            }

            for (int i = 0; i < p.attribute.length; i++) {
                sum[i] = sum[i] + p.attribute[i];
            }
        }
        if (sum != null) {
            for (int i = 0; i < sum.length; i++) {
                sum[i] = sum[i] / cluster.size();
            }
        }

        return new Instance(sum);
    }


    /**
     * assign Instances to clusters
     *
     * @param centroids List<Instance> list of centroids
     * @param dataset   List<Instance> list of all Instances in data set
     * @return HashMap<Instance, List<Instance>> hash map representing the cluster for the corresponding centroid
     */
    private static HashMap<Instance, List<Instance>> assignInstancesToClusters(List<Instance> centroids, Instance[] dataset) {

        HashMap<Instance, List<Instance>> clusters = new HashMap<Instance, List<Instance>>();

        for (Instance p : dataset) {
            double min_distance = Double.MAX_VALUE;
            Instance nearestInstance = null;

            for (Instance centroid : centroids) {
                double distance = calculateDistance(p, centroid);
                if (distance < min_distance) {
                    min_distance = distance;
                    nearestInstance = centroid;
                }
            }

            addClusterMember(clusters, nearestInstance, p);
        }

        return clusters;
    }

    private static void addClusterMember(HashMap<Instance, List<Instance>> cluster, Instance centroid, Instance member) {
        List<Instance> list;

        if (cluster.containsKey(centroid)) {
            list = cluster.get(centroid);
        } else {
            list = new ArrayList<Instance>();
            cluster.put(centroid, list);
        }

        list.add(member);
    }


    /**
     * calculate distance for given two Instances
     *
     * @param p Instance Instance 1
     * @param u Instance Instance 2
     * @return Double euclidean distance between this two Instances
     */
    private static double calculateDistance(Instance p, Instance u) {
        double sum = subtract(p, u);
        return Math.sqrt(sum);
    }


//    /**
//     * print clusters on the console
//     * @param clusters Clusters
//     */
//    private static void printClusters(HashMap<Instance, List<Instance>> clusters) {
//        for(Instance centroid : clusters.keySet()){
//            System.out.println("\nCluster : Size "+clusters.get(centroid).size()+"\n ");
//
////            for(Instance p : clusters.get(centroid)){
////                System.out.println(p.label);
////            }
//        }
//    }


    /**
     * getNormalizedValue calculate the normalized value for x
     *
     * @param x   Double representing the sample
     * @param avg Double representing the average value
     * @param std Double representing the standard deviation value
     * @return Double normalized value
     */
    private static double getNormalizedValue(double x, double avg, double std) {
        return std == 0 ? 0 : (x - avg) / std;
    }


    /**
     * calculate standard deviation
     *
     * @param pts double[] list
     * @param avg double average
     * @return Double standard deviation
     */
    private static double getStandardDeviation(double[] pts, double avg) {
        double sum = 0;
        for (double p : pts) {
            sum = sum + Math.pow(p - avg, 2);
        }

        return Math.sqrt(sum) / (pts.length - 1);
    }


    /**
     * calculate average for the given array
     *
     * @param pts Double[] data
     * @return Double : Average
     */
    private static double getAverage(double[] pts) {
        double sum = 0;
        for (double p : pts) {
            sum = sum + p;
        }

        return sum / pts.length;
    }


}
