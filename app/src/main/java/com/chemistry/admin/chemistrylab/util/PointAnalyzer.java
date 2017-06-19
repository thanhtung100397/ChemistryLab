package com.chemistry.admin.chemistrylab.util;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/1/2016.
 */
public class PointAnalyzer {
    private static final String TAG = "PointAnalyzer";

    public static Point[] analyzePointVertical(Path path) {
        List<Point> listPoint = new ArrayList<>();
        PathMeasure m = new PathMeasure(path, false);
        int length = Math.round(m.getLength());
        Log.i(TAG, "length: " + length);
        int distance = 0;
        float endPoint[] = new float[2];
        float yStartPoint = -1.0f;

        while (distance <= length) {
            m.getPosTan(distance, endPoint, null);
            distance++;
//            Log.i(TAG, "x = " + endPoint[0] + " y = " + endPoint[1]);
            endPoint[0] = Math.round(endPoint[0]);
            endPoint[1] = Math.round(endPoint[1]);
            if (endPoint[1] != yStartPoint) {
                listPoint.add(new Point((int) endPoint[0], (int) endPoint[1]));
                yStartPoint = endPoint[1];
                Log.i(TAG, "x = " + endPoint[0] + " y = " + endPoint[1]);
            }
        }
//
        Log.i(TAG, "-------------------------------------------");
        Log.i(TAG, "size: " + listPoint.size());
        return listPoint.toArray(new Point[listPoint.size()]);
    }

    public static Point[] analyzePointHorizontal(Path path) {
        List<Point> listPoint = new ArrayList<>();
        PathMeasure m = new PathMeasure(path, false);
        int length = Math.round(m.getLength());
        int distance = 0;
        float endPoint[] = new float[2];
        float xStartPoint = -1.0f;

        while (distance <= length) {
            m.getPosTan(distance, endPoint, null);
            distance++;
//            Log.i(TAG, "x = " + endPoint[0] + " y = " + endPoint[1]);
            endPoint[0] = Math.round(endPoint[0]);
            endPoint[1] = Math.round(endPoint[1]);
            if (endPoint[0] != xStartPoint) {
                if(endPoint[0] - xStartPoint != 1){
                    listPoint.add(new Point((int) (endPoint[0] - 1), (int) endPoint[1]));
                    Log.e(TAG, "x = " + (endPoint[0] - 1) + " y = " + endPoint[1]);
                }
                listPoint.add(new Point((int) endPoint[0], (int) endPoint[1]));
                xStartPoint = endPoint[0];
                Log.i(TAG, "x = " + endPoint[0] + " y = " + endPoint[1]);
            }
        }
        Log.i(TAG, "-------------------------------------------");
        Log.i(TAG, "size: " + listPoint.size());
        return listPoint.toArray(new Point[listPoint.size()]);
    }
}
