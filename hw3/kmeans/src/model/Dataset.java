package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jalpanranderi on 3/4/15.
 */
public class Dataset {
    List<Point> data = new ArrayList<Point>();

    public void addPoint(double x, double y){
        data.add(new Point(x, y));
    }

    public List<Point> getPoints(){
        return data;
    }

    public Point getPoint(int i){
        return data.get(i);
    }
}
