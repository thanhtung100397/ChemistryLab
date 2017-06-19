package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument;

import android.content.Context;
import android.graphics.Point;
import android.view.View;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.observer.OnItemDetachListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/11/2016.
 */
public abstract class LaboratorySupportInstrument extends LaboratoryInstrument implements OnItemDetachListener {
    protected int xInGroup, yInGroup;
    protected int totalWidth, totalHeight;
    protected List<LaboratoryInstrument> listItemContain;
    protected List<Point> listDistanceFromParentOfItemContain;

    public LaboratorySupportInstrument(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        this.xInGroup = 0;
        this.yInGroup = 0;
        listItemContain = new ArrayList<>();
        listDistanceFromParentOfItemContain = new ArrayList<>();
    }

    public List<LaboratoryInstrument> getListItemContain() {
        return listItemContain;
    }

    public List<Point> getListDistanceFromParentOfItemContain() {
        return listDistanceFromParentOfItemContain;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public int getXInGroup() {
        return xInGroup;
    }

    public int getYInGroup() {
        return yInGroup;
    }

    @Override
    public void setVisibility(int visibility) {
        for (LaboratoryInstrument instrument : listItemContain) {
            instrument.setVisibility(visibility);
        }
        super.setVisibility(visibility);
    }

    @Override
    public void onItemDetached(LaboratoryInstrument item) {
        listDistanceFromParentOfItemContain.remove(listItemContain.indexOf(item));
        listItemContain.remove(item);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        int length = listItemContain.size();
        for (int i = 0; i < length; i++) {
            listItemContain.get(i).setX(x + listDistanceFromParentOfItemContain.get(i).x);
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        int length = listItemContain.size();
        for (int i = 0; i < length; i++) {
            listItemContain.get(i).setY(y + listDistanceFromParentOfItemContain.get(i).y);
        }
    }

    @Override
    public void onClick(View v) {

    }

    abstract public void addItem(LaboratoryInstrument instrument);

    abstract protected void calculateTotalDimension(int distanceX, int distanceY);

}
