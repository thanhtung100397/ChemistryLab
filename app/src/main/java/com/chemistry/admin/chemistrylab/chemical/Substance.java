package com.chemistry.admin.chemistrylab.chemical;

import android.graphics.Color;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.observer.ContainerCallBack;
import com.chemistry.admin.chemistrylab.tooltip.ItemTip;
import com.chemistry.admin.chemistrylab.util.SymbolConverter;
import com.chemistry.admin.chemistrylab.util.PixelConverter;

import java.io.Serializable;

/**
 * Created by Admin on 8/16/2016.
 */
public abstract class Substance implements Serializable {
    private static final String TAG = "Substance";
    protected String name;
    protected String symbol;
    protected String state;
    protected int color;
    protected String colorHex;
    protected double M;
    protected double density;
    protected double mole;
    protected double maxMoleInHolder;
    protected double width;
    protected ContainerCallBack manager;
    protected ItemTip tip;

    public Substance(String name, String symbol, String state, String colorHex, double M, double density) {
        this.name = name;
        this.symbol = symbol;
        this.state = state;
        this.colorHex = colorHex;
        this.M = M;
        this.density = density;
        this.color = Color.parseColor(colorHex);
    }

    public void setManager(ContainerCallBack manager) {
        this.manager = manager;
        if(manager != null){
            manager.onHeightChange(this, getHeight());
        }
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getState() {
        return state;
    }

    public SpannableString getConvertSymbol() {
        return SymbolConverter.convertToSymbol(symbol);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Substance)) {
            return false;
        }
        Substance temp = (Substance) obj;
        return (this.symbol.equals(temp.getSymbol())) && (this.state.equals(temp.getState()));
    }

    public void calculateMaxMoleInHolder(double holderHeight, double holderWidth) {
        this.maxMoleInHolder = getMole(PixelConverter.calculateVolumeByHeight(holderHeight, holderWidth));
    }

    public double getMaxMoleInHolder() {
        return maxMoleInHolder;
    }

    public void removeFromContainer(){
        if(manager != null){
            manager.onSubstanceRemoved(this);
        }
    }

    /**
     * Set the width of a container which the substance is added to
     * @param width the width of the container
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Split the substance to 2 substance
     * @param mole the amount of mole want to split
     * @return the substance result after splitting
     */
    public Substance split(double mole){
        if(mole <= 0){
            return null;
        }
        Substance result = getClone();
        result.mole = reduceAmount(mole);
        result.tip.update();
        return result;
    }

    /**
     * Add amount of mole for the liquid
     * @param mole the amount of mole want to add
     * @return the amount of mole left which can't be added because the container of the liquid is full
     */
    public double addAmount(double mole){
        if(manager == null){
            this.mole += mole;
            return 0;
        }
        double availableHeightOfHolder = manager.getAvailableHeight();
        double volumeAccepted = getVolume(mole);
        double heightIncrement = PixelConverter.calculateHeightByVolume(volumeAccepted, width);
        double moleAccepted;
        if (availableHeightOfHolder < heightIncrement) {
            moleAccepted = getMole(PixelConverter.calculateVolumeByHeight(availableHeightOfHolder, width));
            heightIncrement = availableHeightOfHolder;
        } else {
            moleAccepted = mole;
        }

        this.mole += moleAccepted;
        manager.onHeightChange(this, heightIncrement);
        return mole - moleAccepted;
    }

    /**
     * Reduce amount of mole of the substance
     * @param mole the amount of mole want to reduce
     * @return the amount of mole lost
     */
    public double reduceAmount(double mole){
        double moleLost;
        if(mole > this.mole){
            moleLost = this.mole;
        }else {
            moleLost = mole;
        }
        this.mole -= moleLost;
        if(manager != null) {
            manager.onHeightChange(this, -PixelConverter.calculateHeightByVolume(getVolume(moleLost), width));
        }
        return moleLost;
    }

    abstract public Substance getClone();

    /**
     * Get the weight by amount of mole
     * @return the weight of the substance (g)
     */
    public double getWeight(double mole) {
        return mole * M;
    }

    /**
     * Get the current weight of the substance
     * @return the current weight of the substance (g)
     */
    public double getWeight(){
        return getWeight(this.mole);
    }

    /**
     * Get the volume by amount of mole
     * @param mole the amount of mole
     * @return the volume of the substance (ml)
     */
    public double getVolume(double mole) {
        return mole * M / density;
    }

    /**
     * Get the current volume of the substance
     * @return the current volume of the substance (ml)
     */
    public double getVolume(){
        return getVolume(this.mole);
    }

    /**
     * Get the current height of the substance
     * @return the current height of the substance
     */
    public double getHeight() {
        return PixelConverter.calculateHeightByVolume(getVolume(this.mole), width);
    }

    /**
     * Get the current mole of the substance
     * @return the current mole of the substance
     */
    public double getMole() {
        return mole;
    }

    /**
     * Get the mole by amount of volume
     * @param volume the amount of volume (ml)
     * @return the mole of the liquid
     */
    public double getMole(double volume) {
        return volume * density / M;
    }

    public double getM() {
        return M;
    }

    public double getDensity() {
        return density;
    }

    public int getColor() {
        return color;
    }

    public ItemTip getTip() {
        return tip;
    }

    public void setTip(ItemTip tip){
        this.tip = tip;
    }
}
