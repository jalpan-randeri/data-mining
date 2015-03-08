package model;

import java.util.Arrays;

/**
 * Created by jalpanranderi on 3/4/15.
 */
public class Instance {
    public double[] attribute;
    public String label;

    public Instance(double[] attrib) {
        attribute = attrib;
    }

    public Instance(double[] attribute, String label) {
        this.attribute = attribute;
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        Instance other;
        if(obj instanceof Instance) {
            other = (Instance) obj;
        }else{
            return false;
        }

        if(attribute.length == other.attribute.length) {
            for (int i = 0; i < attribute.length; i++) {
                if (attribute[i] != other.attribute[i]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(attribute);
    }
}
