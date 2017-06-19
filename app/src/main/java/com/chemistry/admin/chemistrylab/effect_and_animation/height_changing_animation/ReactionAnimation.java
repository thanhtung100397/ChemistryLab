package com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation;

import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionEquation;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionSubstance;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;
import com.chemistry.admin.chemistrylab.tooltip.ItemTip;

import java.util.List;

/**
 * Created by Admin on 9/4/2016.
 */
public class ReactionAnimation implements BaseAnimation {
    public static final String TAG = "ReactionAnimation";
    private List<ReactionSubstance> listReactionSubstances;
    private ItemTip tips[];
    private Substance baseSubstance;
    private LaboratoryHolderInstrument holder;

    public ReactionAnimation(LaboratoryHolderInstrument holder, ReactionEquation equation) {
        this.holder = holder;
        this.listReactionSubstances = equation.getListReactionSubstances();
        int substanceCount = listReactionSubstances.size();
        tips = new ItemTip[substanceCount];
        for (int i = substanceCount - 1; i >= 0; i--) {
            tips[i] = listReactionSubstances.get(i).getSubstance().getTip();
        }
        this.baseSubstance = equation.getBaseReactionSubstance().getSubstance();
    }

    @Override
    public boolean run() {
        if (baseSubstance.getMole() <= 0) {
            return false;
        }
        for (ReactionSubstance aReactionSubstance : listReactionSubstances) {
            aReactionSubstance.doReaction();
        }
        return true;
    }

    @Override
    public void updateUI() {
        int tipCount = tips.length;
        for (int i = tipCount - 1; i >= 0; i--) {
            tips[i].update();
        }
        holder.invalidate();
    }

    @Override
    public void onStop() {
        baseSubstance.reduceAmount(baseSubstance.getMole());
    }

    @Override
    public boolean isPaused() {
        return holder.isAnimationPaused();
    }

    @Override
    public LaboratoryHolderInstrument getHolder() {
        return holder;
    }
}
