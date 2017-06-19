package com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation;

import android.content.Context;

import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.chemical.liquid.LiquidManager;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionEquation;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionSubstance;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;
import com.chemistry.admin.chemistrylab.chemical.solid.SolidManager;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;
import com.chemistry.admin.chemistrylab.tooltip.ItemTip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/9/2016.
 */
public class BoilingAnimation implements BaseAnimation {
    private static final String TAG = "BoilingAnimation";
    private LiquidManager liquidManager;
    protected List<ReactionSubstance> listReactionSubstances;
    protected ItemTip tips[];
    private boolean isStopped;
    private LaboratoryHolderInstrument holder;

    public BoilingAnimation(LaboratoryHolderInstrument holder) {
        this.holder = holder;
        Context context = holder.getContext();
        this.liquidManager = holder.getLiquidManager();
        SolidManager solidManager = holder.getSolidManager();
        List<Substance> listLiquids = liquidManager.getListSubstances();
        int liquidCount = liquidManager.getSubstancesCount();
        listReactionSubstances = new ArrayList<>(liquidCount);

        double moleReducingPerLoop = ReactionEquation.MOLE_CHANGE_PER_LOOP / 4 * liquidCount;

        for (int i = liquidCount - 1; i >= 0; i--) {
            Substance substance = listLiquids.get(i);
            listReactionSubstances.add(new ReactionSubstance(substance, moleReducingPerLoop));
            Solid solidVersionOfThisSubstance = (Solid) DatabaseManager.getInstance(context).getSubstanceBySymbolAndState(substance.getSymbol(), "solid");
            if (solidVersionOfThisSubstance != null) {
                solidVersionOfThisSubstance.reduceAmount(solidVersionOfThisSubstance.getMole());
                solidVersionOfThisSubstance = solidManager.addSubstance(solidVersionOfThisSubstance);
                listReactionSubstances.add(new ReactionSubstance(solidVersionOfThisSubstance, -moleReducingPerLoop));
            }
        }

        int totalSubstances = listReactionSubstances.size();
        tips = new ItemTip[totalSubstances];
        for (int i = 0; i < totalSubstances; i++) {
            tips[i] = listReactionSubstances.get(i).getSubstance().getTip();
        }

        isStopped = false;
    }

    public void stop() {
        isStopped = true;
    }

    @Override
    public boolean run() {
        if (isStopped || liquidManager.getCurrentHeight() <= 0) {
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
