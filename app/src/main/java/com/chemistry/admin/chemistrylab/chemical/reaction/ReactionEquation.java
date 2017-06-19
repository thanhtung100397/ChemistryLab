package com.chemistry.admin.chemistrylab.chemical.reaction;

import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;

import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.util.PixelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/20/2016.
 */
public class ReactionEquation {
    private static final String TAG = "ReactionEquation";
    public static final double MOLE_CHANGE_PER_LOOP = 0.1;//mole
    private List<ReactionSubstance> listReactionSubstances;
    private ReactionSubstance baseReactionSubstance;
    private int numberOfStartSubstance;
    private boolean hasGasCreated;

    public ReactionEquation() {
        this.listReactionSubstances = new ArrayList<>();
        this.numberOfStartSubstance = 0;
        this.hasGasCreated = false;
    }

    public CharSequence getEquation() {
        CharSequence result = new SpannableString("");
        int substanceCount = listReactionSubstances.size();
        for (int i = 0; i < substanceCount; i++) {
            Log.i(TAG, "getEquation: "+listReactionSubstances.size()+" "+listReactionSubstances.toString());
            result = TextUtils.concat(result, listReactionSubstances.get(i).getSubstance().getConvertSymbol());
            if (i == numberOfStartSubstance - 1) {
                result = TextUtils.concat(result, " = ");
            } else if (i != substanceCount - 1) {
                result = TextUtils.concat(result, " + ");
            }
        }
        return result;
    }

    public void addReactionSubstance(ReactionSubstance reactionSubstance) {
        if (reactionSubstance != null) {
            listReactionSubstances.add(reactionSubstance);
        }
    }

    public void setUpReaction() {
        ReactionSubstance reactionSubstance1 = listReactionSubstances.get(0);
        ReactionSubstance reactionSubstance2 = listReactionSubstances.get(1);
        this.numberOfStartSubstance = 2;

        if (reactionSubstance1.getMolePerBalanceIndex() >= reactionSubstance2.getMolePerBalanceIndex()) {
            baseReactionSubstance = reactionSubstance1;
        } else {
            baseReactionSubstance = reactionSubstance2;
        }
        baseReactionSubstance.setMoleChangingPerLoop(-MOLE_CHANGE_PER_LOOP);

        for (ReactionSubstance aReactionSubstance : listReactionSubstances) {
            aReactionSubstance.calculateMoleChangingPerLoop(baseReactionSubstance);
        }
    }

    public List<ReactionSubstance> getListReactionSubstances() {
        return listReactionSubstances;
    }

    public ReactionSubstance getBaseReactionSubstance() {
        return baseReactionSubstance;
    }

    public int getNumberOfStartSubstance() {
        return numberOfStartSubstance;
    }

    public boolean hasGasCreated() {
        return hasGasCreated;
    }

    public void setHasGasCreated(boolean value) {
        this.hasGasCreated = value;
    }
}
