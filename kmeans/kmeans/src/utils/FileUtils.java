package utils;

import model.Dataset;
import model.Instance;

import java.io.*;

/**
 * FileUtils common utilities for the file operations
 *
 * Created by jalpanranderi on 2/16/15.
 */
public class FileUtils {

    public static int[] readRandomFile(String path) throws IOException {
        int[] random = null;

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;


        while((line = reader.readLine()) != null){
            String[] nos = line.split(",");
            random = new int[nos.length];
            for(int i = 0; i < random.length; i++){
                random[i] = Integer.parseInt(nos[i].trim());
            }
        }
        reader.close();

        return random;
    }


    /**
     * readFile reads the arff file and converts the file into Question models
     * @param path String representing the path of the file
     * @return Dataset representing the model of data
     * @throws java.io.IOException when file not found or there is error in IO because of permissions
     */
    public static Dataset readFile(String path) throws IOException {
        int size = getLineNumber(path);
        Instance[] input = new Instance[size];

        int index = 0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        boolean isSkipable = true;

        while((line = reader.readLine()) != null){
            if(!isSkipable){
                input[index] = parseLine(line);
                index++;
            }else if(line.equals("@data")) {
                isSkipable = false;
            }
        }
        reader.close();

        return new Dataset(input);
    }


    /**
     * getLineNumber determines the total number of data lines from arff file
     * @param path String representing the file path
     * @return Integer representing the total number of data lines
     * @throws java.io.IOException when file not found or error in IO due to insufficient permissions.
     */
    private static int getLineNumber(String path) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(path));
        int diff = 0;

        String line;

        while((line = reader.readLine()) != null){
            if(line.equals("@data")){
                diff++;
                break;
            }else{
                diff++;
            }
        }

        reader.skip(Long.MAX_VALUE);
        int count = reader.getLineNumber()  - diff;
        reader.close();

        return count;
    }


    /**
     * parseLine parses the line and append the value into model
     * @param line String representing the actual text from the file
     * @return Instance model the line into Question
     */
    private static Instance parseLine(String line) {
        // 4 points 0-3
        // 1 label 4th index
        String[] contents = line.split(",");

        double[] attrib = new double[contents.length - 1];

        for(int i = 0; i < attrib.length; i++){
            attrib[i] = Double.parseDouble(contents[i]);
        }


        return new Instance(attrib, contents[attrib.length]);
    }


    /**
     * writeFile writes the content of the file on the disk
     * @param content String representing the contents
     * @param file_name String representing the path where the file is created
     * @throws java.io.IOException when location is not available or user has no permission create file
     */
    public static void writeFile(String content, String file_name) throws IOException {
        FileWriter writer = new FileWriter(file_name);
        writer.write(content);
        writer.close();
    }
}
