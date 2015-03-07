
import model.Dataset;
import model.Point;
import utils.FileUtils;

import java.io.IOException;
import java.util.*;

/**
 *
 * Input : K
 *
 * Step 1: choose k random points from the input set
 * Step 2: for each point : Data set D
 *             assign the Ci for which the dist(ci, point) is minimum
 *
 * Step 3: for each Cluster Ci : C
 *             calculate mean point, and assign it to Ci
 * Step 4: Check if newCi === C for all Ci in C
 * Step 5: if true break and return the clusters else goto step 2,
 *
 *
 * Created by jalpanranderi on 3/4/15.
 */
public class KMeans {
    public static void main(String[] args) throws IOException {
        Dataset dataset = FileUtils.readFile("input/dataset.txt");

        preprocess(dataset);
        // randomly choose initial centroids
        List<Point> centroids = new ArrayList<Point>();
        centroids.add(dataset.getPoint(27));
        centroids.add(dataset.getPoint(12));

        findClusters(dataset, centroids);
    }

    private static void preprocess(Dataset dataset) {

    }

    /**
     * find clusters calcuate the clusters for the given centroids
     * @param dataset List<Point>
     * @param centroids List<Point>
     * @return List<List<Point>> representing the list of clusters
     */
    private static Collection<List<Point>> findClusters(Dataset dataset, List<Point> centroids){


        List<Point> calculateCentroids = new ArrayList<Point>();
        HashMap<Point, List<Point>> clusters = new HashMap<Point, List<Point>>();

        while (!isCentroidSame(calculateCentroids, centroids)) {

            // update only if centroids are already calculated
            if(calculateCentroids.size() != 0) {
                centroids = calculateCentroids;
            }


            clusters = assignPointsToClusters(centroids, dataset.getPoints());
            calculateCentroids = calcuateCentroids(clusters.values());

        }

        printClusters(clusters);
        return  clusters.values();
    }

    /**
     * calculate centroids
     * @param clusters Collection<Point, <Point>> determine the centroid for the given each cluster
     * @return List<Point> list of centroids for the given list of clusters
     */
    private static List<Point> calcuateCentroids(Collection<List<Point>> clusters){
        ArrayList<Point> centroids  = new ArrayList<Point>();

        for(List<Point> cluser : clusters){
            Point centroid  = calculateMean(cluser);
            centroids.add(centroid);
        }

        return centroids;
    }


    /**
     * return true iff the both list are same
     * @param calculated List<Point> list of centroids
     * @param old List<Point> list of centroids
     * @return Boolean
     */
    private static boolean isCentroidSame(List<Point> calculated, List<Point> old){
        if(calculated.size() == 0){
            return false;
        }else{

            HashSet<Point> oldCentroids = new HashSet<Point>();
            oldCentroids.addAll(old);

            for(Point c : calculated){
                if(!oldCentroids.contains(c)){
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * calculate mean for the given list
     * @param cluster List<Point>
     * @return Point representing the average value for x and y
     */
    private static Point calculateMean(List<Point> cluster) {
        double x = 0,y = 0;
        for (Point p : cluster){
            x = x + p.mX;
            y = y + p.mY;
        }

        return  new Point(x/cluster.size(), y/cluster.size());
    }


    /**
     * assign points to clusters
     * @param centroids List<Point> list of centroids
     * @param dataset List<Point> list of all points in data set
     * @return HashMap<Point, List<Point>> hash map representing the cluster for the corresponding centroid
     */
    private static HashMap<Point, List<Point>> assignPointsToClusters(List<Point> centroids, List<Point> dataset) {

        HashMap<Point, List<Point>> clusters = new HashMap<Point, List<Point>>();

        for(Point p : dataset){
            double min_distance = Double.MAX_VALUE;
            Point nearestPoint = null;

            for(Point centroid: centroids){
                double distance = calculateDistance(p, centroid);
                if(distance < min_distance){
                    min_distance = distance;
                    nearestPoint = centroid;
                }
            }

            addClusterMember(clusters, nearestPoint, p);
        }

        return clusters;
    }

    private static void addClusterMember(HashMap<Point, List<Point>> cluster, Point centroid, Point member){
        List<Point> list = null;

        if(cluster.containsKey(centroid)) {
            list = cluster.get(centroid);
        }else{
            list = new ArrayList<Point>();
            cluster.put(centroid, list);
        }

        list.add(member);
    }


    /**
     * calculate distance for given two points
     * @param p Point point 1
     * @param u Point point 2
     * @return Double euclidean distance between this two points
     */
    private static double calculateDistance(Point p, Point u) {
        return Math.pow((p.mX - u.mX),2) + Math.pow((p.mY - u.mY), 2);
    }


    /**
     * print clusters on the console
     * @param clusters Clusters
     */
    private static void printClusters(HashMap<Point, List<Point>> clusters) {
        for(Point centroid : clusters.keySet()){
            System.out.println("\nCluster\n");
            for(Point p : clusters.get(centroid)){
                System.out.printf("%.1f\t%.1f\n",p.mX, p.mY);
            }
        }
    }

}
