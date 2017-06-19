package com.chemistry.admin.chemistrylab.chemical;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionEquation;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.GasBottle;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.observer.OnReactionListener;
import com.chemistry.admin.chemistrylab.observer.ContainerCallBack;
import com.chemistry.admin.chemistrylab.util.PixelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/19/2016.
 */

public abstract class BaseSubstanceManager implements ContainerCallBack {
    protected Context context;
    protected LaboratoryHolderInstrument holder;
    protected OnReactionListener onReactionListener;
    protected List<Substance> listSubstances;
    protected double currentHeight;
    protected double maxHeight;
    protected double width;

    public BaseSubstanceManager(Context context, LaboratoryHolderInstrument holder) {
        this.context = context;
        this.holder = holder;
        this.listSubstances = new ArrayList<>();
        this.maxHeight = holder.getContainedSpaceHeight();
        this.width = holder.getContainedSpaceWidth();
    }

    public void setOnReactionListener(OnReactionListener onReactionListener) {
        this.onReactionListener = onReactionListener;
    }

    public Substance addSubstance(Substance substance) {
        if (listSubstances.contains(substance)) {
            Substance substanceAdded = listSubstances.get(listSubstances.indexOf(substance));
            substanceAdded.addAmount(substance.getMole());
            substanceAdded.getTip().update();
            return substanceAdded;
        }
        holder.getToolTip().addItem(substance.getTip());
        listSubstances.add(substance);
        return substance;
    }

    public List<Substance> getListSubstances() {
        return listSubstances;
    }

    public int getSubstancesCount() {
        return listSubstances.size();
    }

    public Substance getSubstance(int index) {
        if (index < 0 || listSubstances.size() - 1 < index) {
            return null;
        }
        return listSubstances.get(index);
    }

    public List<ReactionEquation> checkReaction(Substance substance) {
        List<ReactionEquation> listEquationResult = new ArrayList<>();
        if (listSubstances.size() == 0 ||
                (substance.getClass() == Solid.class &&
                        getSubstance(0).getClass() == Solid.class)) {
            return listEquationResult;
        }
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        boolean isBoiling = holder.isBoiling();
        for (int i = listSubstances.size() - 1; i >= 0; i--) {
            if(listSubstances.get(i) != substance) {
                ReactionEquation equation = databaseManager.findReaction(substance, listSubstances.get(i), isBoiling);
                if (equation != null) {
                    listEquationResult.add(equation);
                }
            }
        }
        return listEquationResult;
    }

    public abstract void drawAllSubstances(Canvas canvas, Paint paint);

    public void pourAllSubstancesInto(LaboratoryHolderInstrument newHolder) {
        if (listSubstances.size() == 0) {
            return;
        }
        BaseSubstanceManager newSubstanceManager = getSuitableSubstanceManager(newHolder);
        double emptyHeightOfNewHolder = newSubstanceManager.getEmptyHeight();
        double widthOfNewHolder = newSubstanceManager.getWidth();
        if (emptyHeightOfNewHolder > 0) {
            List<Substance> listSubstancesTaken = takeSubstanceByHeightAndWidth(emptyHeightOfNewHolder, widthOfNewHolder);
            int substanceCount = listSubstancesTaken.size();
            for (int i = 0; i < substanceCount; i++) {
                newSubstanceManager.addSubstance(listSubstancesTaken.get(i));
            }
        }
    }

    public abstract BaseSubstanceManager getSuitableSubstanceManager(LaboratoryHolderInstrument holder);

    public List<Substance> takeSubstanceByHeightAndWidth(double height, double width) {
        double volumeTaken = PixelConverter.calculateVolumeByHeight(height, width);
        double heightConverted = PixelConverter.calculateHeightByVolume(volumeTaken, this.width);

        return takeSubstanceByHeight(heightConverted);
    }

    protected abstract List<Substance> takeSubstanceByHeight(double height);

    public List<Substance> clearAllSubstances() {
        int size = listSubstances.size();
        List<Substance> result = new ArrayList<>(size);
        for (int i = size - 1; i >= 0; i--) {
            result.add(removeSubstance(i));
        }
        return result;
    }

    public Substance removeSubstance(int index) {
        Substance removedSubstance = listSubstances.remove(index);
        removedSubstance.getTip().destroy();
        removedSubstance.setManager(null);
        this.currentHeight -= removedSubstance.getHeight();
        return removedSubstance;
    }

    public Substance removeSubstance(Substance substance) {
        int size = listSubstances.size();
        Substance removedSubstance = null;
        for (int i = 0; i < size; i++) {
            if(listSubstances.get(i) == substance){
                removedSubstance = listSubstances.remove(i);
                removedSubstance.getTip().destroy();
                removedSubstance.setManager(null);
                this.currentHeight -= removedSubstance.getHeight();
                break;
            }
        }
        return removedSubstance;
    }

    @Override
    public void onSubstanceRemoved(Substance substance) {
        removeSubstance(substance);
    }

    @Override
    public void onHeightChange(Substance substance, double valueChange) {
//        if (substance.getMole() == 0) {
//            onSubstanceRemoved(substance);
//        }
    }

    @Override
    public boolean isClosedVessel() {
        return holder instanceof GasBottle;
    }

    @Override
    public double getAvailableHeight() {
        return getEmptyHeight();
    }

    public double getWidth() {
        return width;
    }

    public double getCurrentHeight() {
        return currentHeight;
    }

    public double getEmptyHeight() {
        return maxHeight - currentHeight;
    }
}
