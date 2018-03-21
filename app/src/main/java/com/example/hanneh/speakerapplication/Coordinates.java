package com.example.hanneh.speakerapplication;

import java.util.ArrayList;

/**
 * Created by hanneh on 3/20/18.
 */

public class Coordinates {

    static ArrayList<Float> points;

    public void addPoint(float x, float y){
        points.add(x);
        points.add(y);
    }

    public static float[] getPoints(){

        float[] floats = new float[points.size()];
        int i = 0;

        for( Float f : points){
            floats[i++] = points.get(i);

        }

        return floats;



    }
}

