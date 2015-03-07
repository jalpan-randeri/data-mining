package model;

/**
 * Created by jalpanranderi on 3/4/15.
 */
public class Point {
    public double mX;
    public double mY;

    public Point(double x, double y) {
        this.mX = x;
        this.mY = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (Double.compare(point.mX, mX) != 0) return false;
        if (Double.compare(point.mY, mY) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(mX);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return  String.format("(%.1f, %.1f)",mX, mY);
    }
}
