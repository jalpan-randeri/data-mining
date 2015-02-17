package utils;

import model.Question;

import java.io.*;

/**
 * FileUtils common utilities for the file operations
 *
 * Created by jalpanranderi on 2/16/15.
 */
public class FileUtils {

    /**
     * readFile reads the arff file and converts the file into Question models
     * @param path String representing the path of the file
     * @return Question[] representing the model of data
     * @throws IOException when file not found or there is error in IO because of permissions
     */
    public static Question[] readFile(String path) throws IOException {
        int size = getLineNumber(path);
        Question[] input = new Question[size];
        int index = 0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
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

        return input;
    }


    /**
     * parseLine parses the line and append the value into model
     * @param line String representing the actual text from the file
     * @return Question model the line into Question
     */
    private static Question parseLine(String line) {
        // 4 points 0-3
        // 1 label 4th index
        String[] contents = line.split(",");


        Question question = new Question();
        question.mDatapoints = new double[4];
        for(int i = 0; i < question.mDatapoints.length; i++){
            question.mDatapoints[i] = Double.parseDouble(contents[i]);
        }


        if(contents.length > question.mDatapoints.length) {
            question.mLabel = contents[question.mDatapoints.length];
        }

        return question;


    }


    /**
     * getLineNumber determines the total number of data lines from arff file
     * @param path String representing the file path
     * @return Integer representing the total number of data lines
     * @throws IOException when file not found or error in IO due to insufficient permissions.
     */
    private static int getLineNumber(String path) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(path));
        int diff = 0;

        String line = null;

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
     * writeFile writes the content of the file on the disk
     * @param content String representing the contents
     * @param file_name String representing the path where the file is created
     * @throws IOException when location is not available or user has no permission create file
     */
    public static void writeFile(String content, String file_name) throws IOException {
        FileWriter writer = new FileWriter(file_name);
        writer.write(content);
        writer.close();
    }
}
