package utils;

import java.util.Random;

/**
 * Created by jalpanranderi on 1/30/15.
 */
public class RandomUtils {

    /**
     * fill the array with natural series starting with 0
     * @param data
     */
    public static void fillSeries(int[] data){
        for(int i = 0; i < data.length; i++){
            data[i] = i;
        }
    }


    /**
     * shuffle the given array
     * @param indexes int[] array representing integer indexes
     */
    public static void shuffle(int[] indexes){
        Random r = new Random();
        for(int i = indexes.length - 1; i > 0; i--){
            int pos = r.nextInt(indexes.length) % i;
            swap(indexes, i, pos);
        }
    }

    private static void swap(int[] indexes, int src, int tar) {
        int temp = indexes[src];
        indexes[src] = indexes[tar];
        indexes[tar] = temp;
    }
}
