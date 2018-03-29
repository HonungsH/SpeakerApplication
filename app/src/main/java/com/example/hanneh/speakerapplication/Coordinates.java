package com.example.hanneh.speakerapplication;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by hanneh on 3/20/18.
 */

public class Coordinates {

    static ArrayList<Float> points;
    static String [] ips;

    //Add new coordinates to ArrayList
    public void addPoint(float x, float y){
        points.add(x);
        points.add(y);
    }

    public Point createPoint(int x, int y){
        Point p = new Point(x, y);
        return p;
    }


    //Retrieve from server and then add to the ArrayList
    public void getCoordinateFromServer(float x0, float y0){
        addPoint(x0, y0);
    }


    //Make arrayList to float vector so drawpoints method in MapActivity can draw it.
    public static float[] getPoints(){

        float[] floats = new float[points.size()];
        int i = 0;

        for( Float f : points){
            floats[i++] = points.get(i);

        }

        return floats;



    }
}

