package model;


/**
 * Created by jalpanranderi on 3/4/15.
 */
public class Dataset {
    private Instance[] data;

    public Dataset(Instance[] data) {
        this.data = data;
    }

    public Instance[] getPoints(){
        return data;
    }

    public Instance getPoint(int i){
        if(data != null && i < data.length && i >= 0) {
            return data[i];
        }else{
            return null;
        }
    }
}
