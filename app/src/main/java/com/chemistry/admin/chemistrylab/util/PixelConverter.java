package com.chemistry.admin.chemistrylab.util;

/**
 * Created by Admin on 9/4/2016.
 */
public class PixelConverter {
    public static final int PIXEL_SQUARE_PER_ML = 120;// 1 ml = 120 px^2

    public static double calculateHeightByVolume(double volume, double widthSpace){
        return volume * PIXEL_SQUARE_PER_ML / widthSpace;
    }

    public static double calculateVolumeByHeight(double height, double widthSpace){
        return height * widthSpace * 1.0/ PIXEL_SQUARE_PER_ML;
    }
}
