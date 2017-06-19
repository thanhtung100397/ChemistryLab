package com.chemistry.admin.chemistrylab.chemical.solid;

import android.graphics.Point;

import com.chemistry.admin.chemistrylab.chemical.Substance;

/**
 * Created by Admin on 8/22/2016.
 */
public class Solid extends Substance {
    //    public static final double DEFAULT_MOLE = 3;//6 gram
    private static final String TAG = "solid";

    /**
     * @param name     name of the solid
     * @param symbol   the symbol of the solid
     * @param colorHex the color of the solid
     * @param M        the M of the solid (g/mole)
     * @param density  the density of the solid (g/ml)
     * @param weight   the weight of the solid (g)
     */
    public Solid(String name, String symbol, String colorHex, double M, double density, double weight) {
        super(name, symbol, TAG, colorHex, M, density);
        this.mole = weight / M;
    }

    @Override
    public Solid getClone() {
        Solid solid = new Solid(name, symbol, colorHex, M, density, getWeight());
        solid.setTip(tip.getClone(solid));
        return solid;
    }

    /**
     * Split the solid to 2 solid
     *
     * @param mole the amount of mole want to split
     * @return the solid result after splitting
     */
    @Override
    public Solid split(double mole) {
        return (Solid) super.split(mole);
    }

    public double getMole(double weight) {
        return weight / M;
    }

    @Override
    public String toString() {
        return super.toString() + " weight: " + getWeight() + " height: " + getHeight();
    }

    public Point getSurfaceLine() {
        if (manager != null) {
            return manager.getSurfaceLine(this);
        }
        return null;
    }

    public int getYTop() {
        if (manager != null) {
            return (int) manager.getYTop(this);
        }
        return -1;
    }
}
