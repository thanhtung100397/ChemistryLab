package com.chemistry.admin.chemistrylab.chemical.gas;

import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.observer.ContainerCallBack;

/**
 * Created by Admin on 8/22/2016.
 */
public class Gas extends Substance {
    public static final String COLORLESS_HEX = "#00000000";
    public static final double STANDARD_CONDITION_CONSTANT = 22.4;
    public static final double DEFAULT_VOLUME = 224000;//224000ml
    public static final double UNCHANGED_ALPHA_VOLUME = DEFAULT_VOLUME * 6;
    public static final int HIGHEST_ALPHA = 200;
    public static final double ALPHA_PER_ML = HIGHEST_ALPHA * 1.0 / UNCHANGED_ALPHA_VOLUME;
    public static final String TAG = "gas";
    private boolean isColorless;

    public Gas(String name, String symbol, String colorHex, double M, double density, double volume) {
        super(name, symbol, TAG, colorHex, M, density);
        this.isColorless = colorHex.equals(COLORLESS_HEX);
        this.mole = getMole(volume);
        this.maxMoleInHolder = 15.0;
    }

    @Override
    public Gas split(double mole) {
        return (Gas) super.split(mole);
    }

    @Override
    public void calculateMaxMoleInHolder(double holderHeight, double holderWidth) {
        this.maxMoleInHolder = 15.0;
    }

    @Override
    public double addAmount(double mole) {
        if (manager == null) {
            this.mole += mole;
            return 0;
        }

        if (manager.isClosedVessel()) {
            this.mole += mole;
            return 0;
        }
        return mole;
    }

    @Override
    public double reduceAmount(double mole) {
        double moleLost;
        if (mole > this.mole) {
            moleLost = mole - this.mole;
            this.mole = 0;
            if (manager != null) {
                manager.onSubstanceRemoved(this);
            }
            return moleLost;
        } else {
            moleLost = mole;
            this.mole -= mole;
        }
        return moleLost;
    }

    @Override
    public Gas getClone() {
        Gas gas = new Gas(name, symbol, colorHex, M, density, getVolume());
        gas.setTip(tip.getClone(gas));
        return gas;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getVolume(double mole) {
        return mole * STANDARD_CONDITION_CONSTANT * 1000;
    }

    @Override
    public double getMole(double volume) {
        return volume / (1000 * STANDARD_CONDITION_CONSTANT);
    }

    public int getAlpha() {
        if (getVolume() < UNCHANGED_ALPHA_VOLUME) {
            return (int) Math.round(getVolume() * ALPHA_PER_ML);
        }
        return HIGHEST_ALPHA;
    }

    public boolean isColorless() {
        return isColorless;
    }
}
