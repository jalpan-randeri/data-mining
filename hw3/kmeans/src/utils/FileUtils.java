package utils;

import model.Dataset;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jalpanranderi on 3/4/15.
 */
public class FileUtils {

    public static Dataset readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        Dataset dataset = new Dataset();
        while((line = reader.readLine()) != null){
            parseLine(line, dataset);
        }


        return dataset;
    }

    private static void parseLine(String line, Dataset dataset) {
        String[] pts = line.split(",");

        if(pts.length % 2 == 0) {

            for (int i = 0; i < pts.length; i += 2) {
                int x = Integer.parseInt(pts[i].trim());
                int y = Integer.parseInt(pts[i + 1].trim());
                dataset.addPoint(x, y);
            }
        }else{
            System.err.println("Data set is of odd length");
        }
    }
}
