package utils;

import model.Question;

import java.io.*;

/**
 * Created by jalpanranderi on 2/16/15.
 */
public class FileUtils {
    public static Question[] readFile(String path) throws IOException {
        int size = getLineNumber(path);
        Question[] input = new Question[size];
        int index = 0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        boolean isSkipable = true;

        while((line = reader.readLine()) != null){
            if(!isSkipable){
                parseLine(line, index, input);
                index++;
            }else if(line.equals("@data")) {
                isSkipable = false;
            }
        }
        reader.close();

        return input;
    }

    private static void parseLine(String line, int index, Question[] input) {
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

        input[index] = question;


    }


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

}
