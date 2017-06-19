package com.chemistry.admin.chemistrylab.chemical.solid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.chemistry.admin.chemistrylab.chemical.BaseSubstanceManager;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/23/2016.
 */
public class SolidManager extends BaseSubstanceManager {
    private static final String TAG = "SolidManager";
    private Point holderArrPoint[];

    public SolidManager(Context context, LaboratoryHolderInstrument holder) {
        super(context, holder);
        this.holderArrPoint = holder.getArrayPoint();
        this.currentHeight = 0;
    }

    @Override
    public Solid addSubstance(Substance solid) {
        Solid result = (Solid) super.addSubstance(solid);
        if (result == solid) {
            result.setWidth(width);
            result.calculateMaxMoleInHolder(maxHeight, width);
            result.setManager(this);
            holder.checkReaction(result);
        }
        return result;
    }

    @Override
    public void drawAllSubstances(Canvas canvas, Paint paint) {
        int startIndex = (int) maxHeight;
        int endIndex;
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < listSubstances.size(); i++) {
            Solid aSolid = getSubstance(i);
            paint.setColor(aSolid.getColor());
            endIndex = startIndex - ((int) aSolid.getHeight());
            for (int j = startIndex; j > endIndex; j--) {
                Point line = holderArrPoint[j];
                canvas.drawLine(line.x, j, line.y, j, paint);
            }
            startIndex = endIndex;
        }
    }

    @Override
    public Solid getSubstance(int index) {
        return (Solid) super.getSubstance(index);
    }

    @Override
    public BaseSubstanceManager getSuitableSubstanceManager(LaboratoryHolderInstrument holder) {
        return holder.getSolidManager();
    }

    @Override
    protected List<Substance> takeSubstanceByHeight(double height) {
        if(height >= this.currentHeight){
            return clearAllSubstances();
        }
        List<Substance> result = new ArrayList<>(listSubstances.size());
        int lastSubstanceIndex = listSubstances.size() - 1;
        while (height > 0){
            Solid solid = getSubstance(lastSubstanceIndex);
            double solidHeight = solid.getHeight();
            if(height >= solidHeight){
                result.add(removeSubstance(lastSubstanceIndex));
            }else {
                double ratio = height / solidHeight;
                result.add(solid.split(solid.getMole() * ratio));
                solid.getTip().update();
            }
            height -= solidHeight;
            lastSubstanceIndex--;
        }

        return result;
    }

    @Override
    public void onHeightChange(Substance solid, double valueChange) {
        this.currentHeight += valueChange;
        super.onHeightChange(solid, valueChange);
    }

    @Override
    public Point getSurfaceLine(Substance substance) {
        return holderArrPoint[(int) getYTop(substance)];
    }

    @Override
    public double getYTop(Substance substance) {
        double result = maxHeight;
        for (int i = 0; i < listSubstances.size(); i++) {
            Substance temp = listSubstances.get(i);
            result -= temp.getHeight();
            if (temp == substance) {
                return result;
            }
        }
        return -1;
    }
}
