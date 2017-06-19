package com.chemistry.admin.chemistrylab.chemical.liquid;

import com.chemistry.admin.chemistrylab.chemical.Substance;

/**
 * Created by Admin on 8/22/2016.
 */
public class Liquid extends Substance {
    //    public static final float DEFAULT_VOLUME = 100;//100ml
    public static final int HIGHEST_ALPHA = 200;
    public static final double ALPHA_PER_ML = HIGHEST_ALPHA * 1.0 / 500;
    private static final String TAG = "liquid";

    /**
     *
     * @param name name of the liquid
     * @param symbol the symbol of the liquid
     * @param colorHex the color of the liquid
     * @param M the M of the liquid (g/mole)
     * @param density the density of the liquid (g/ml)
     * @param volume the volume of the liquid (ml)
     */
    public Liquid(String name, String symbol, String colorHex, double M, double density, double volume) {
        super(name, symbol, TAG, colorHex, M, density);
        this.mole = getMole(volume);
    }

    @Override
    public Liquid split(double mole) {
        return (Liquid) super.split(mole);
    }

    /**
     * Get alpha value of the liquid at current volume
     * @return
     */
    public int getAlpha() {
        return (int) Math.round(getVolume() * ALPHA_PER_ML);
    }

    @Override
    public Liquid getClone() {
        Liquid liquid = new Liquid(name, symbol, colorHex, M, density, getVolume());
        liquid.setTip(tip.getClone(liquid));
        return liquid;
    }

    @Override
    public String toString() {
        return super.toString() + " volume: " + getVolume() + " height: " + getHeight();
    }
}
