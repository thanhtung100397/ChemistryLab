package com.chemistry.admin.chemistrylab.chemical.reaction;

import com.chemistry.admin.chemistrylab.chemical.Substance;

/**
 * Created by Admin on 10/2/2016.
 */
public class ReactionSubstance {
    private Substance substance;
    private int balanceIndex;
    private double moleChangingPerLoop;

    public ReactionSubstance(Substance substance, int balanceIndex) {
        this.substance = substance;
        this.balanceIndex = balanceIndex;
    }

    public ReactionSubstance(Substance substance, double moleReducingPerLoop) {
        this.substance = substance;
        this.moleChangingPerLoop = -moleReducingPerLoop;
    }

    public Substance getSubstance() {
        return substance;
    }

    public int getBalanceIndex() {
        return balanceIndex;
    }

    public double getMoleChangingPerLoop() {
        return moleChangingPerLoop;
    }

    public void setMoleChangingPerLoop(double moleChangingPerLoop) {
        this.moleChangingPerLoop = moleChangingPerLoop;
    }

    public void calculateMoleChangingPerLoop(ReactionSubstance baseReactionSubstance) {
        this.moleChangingPerLoop = baseReactionSubstance.getMoleChangingPerLoop() * balanceIndex / baseReactionSubstance.getBalanceIndex();
    }

    public double getMolePerBalanceIndex() {
        return substance.getMole() / balanceIndex;
    }

    public void doReaction() {
        if (moleChangingPerLoop > 0) {
            substance.addAmount(moleChangingPerLoop);
        } else {
            substance.reduceAmount(-moleChangingPerLoop);
        }
    }

    public void setSubstance(Substance substance) {
        this.substance = substance;
    }

    @Override
    public String toString() {
        if (balanceIndex > 1) {
            return Math.abs(balanceIndex) + substance.getSymbol();
        }
        return substance.getSymbol();
    }
}
