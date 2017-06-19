package com.chemistry.admin.chemistrylab.chemical.gas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.chemistry.admin.chemistrylab.chemical.BaseSubstanceManager;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;

import java.util.List;

/**
 * Created by Admin on 8/23/2016.
 */
public class GasManager extends BaseSubstanceManager {
    private Path gasPath;

    public GasManager(Context context, LaboratoryHolderInstrument holder) {
        super(context, holder);
    }

    public Path getGasPath() {
        return gasPath;
    }

    public void setGasPath(Path gasPath) {
        this.gasPath = gasPath;
    }

    @Override
    public Gas addSubstance(Substance substance) {
        Gas result = (Gas) super.addSubstance(substance);
        if(result == substance){
            result.setManager(this);
        }
        return result;
    }

    //    public void findReaction() {
//        for (int i = 0; i < gasCount - 1; i++) {
//            for (int j = 1; j < gasCount; j++) {
//                Gas gas1 = listGas.get(i);
//                Gas gas2 = listGas.get(j);
//                ReactionEquation result = DatabaseManager.getInstance(context).findReaction(gas1, gas2);
//                if (result != null) {
//                    onReactionListener.reactionHappened(result.getEquation());
//                    listGas.remove(gas1);
//                    listGas.remove(gas2);
//                    gasCount -= 2;
//                    List<Substance> listResultSubstance = result.getResultSubstances();
//                    int length = listResultSubstance.size();
//                    for (int k = 0; k < length; k++) {
//                        if (listResultSubstance.get(k) instanceof Gas) {
//                            addGas((Gas) listResultSubstance.get(k));
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    @Override
    public void drawAllSubstances(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (gasPath != null) {
            for (int i = listSubstances.size() - 1; i >= 0; i--) {
                Gas gas = getSubstance(i);
                paint.setColor(gas.getColor());
                if (!gas.isColorless()) {
                    paint.setAlpha(gas.getAlpha());
                }
                canvas.drawPath(gasPath, paint);
            }
        }
    }

    @Override
    public Gas getSubstance(int index) {
        return (Gas) super.getSubstance(index);
    }

    @Override
    public void onHeightChange(Substance gas, double valueChange) {

    }

    @Override
    public double getAvailableHeight() {
        return maxHeight;
    }

    @Override
    public BaseSubstanceManager getSuitableSubstanceManager(LaboratoryHolderInstrument holder) {
        return holder.getGasManager();
    }

    @Override
    protected List<Substance> takeSubstanceByHeight(double height) {
        return clearAllSubstances();
    }

    @Override
    public Point getSurfaceLine(Substance substance) {
        return new Point(0, 0);
    }

    @Override
    public double getYTop(Substance substance) {
        return 0;
    }
}
