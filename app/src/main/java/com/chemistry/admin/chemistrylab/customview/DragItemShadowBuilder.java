package com.chemistry.admin.chemistrylab.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.LaboratorySupportInstrument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/24/2016.
 */
public class DragItemShadowBuilder extends View.DragShadowBuilder {
    private static final String TAG = "ShadowBuilder";
    private List<Bitmap> listShadowImages;
    private List<Point> listShadowImagesLocation;
    private int widthShadow, heightShadow;
    private int yShadowTouch;

    public DragItemShadowBuilder(LaboratoryInstrument instrument) {
        listShadowImages = new ArrayList<>();
        listShadowImagesLocation = new ArrayList<>();
        listShadowImages.add(instrument.createReviewImage(instrument.getHeightView()));

        if (instrument instanceof LaboratorySupportInstrument) {
            LaboratorySupportInstrument supportInstrument = (LaboratorySupportInstrument) instrument;
            listShadowImagesLocation.add(new Point(supportInstrument.getXInGroup(), supportInstrument.getYInGroup()));
            List<LaboratoryInstrument> listItem = supportInstrument.getListItemContain();
            int length = listItem.size();
            for (int i = 0; i < length; i++) {
                LaboratoryInstrument item = listItem.get(i);
                listShadowImages.add(item.createReviewImage(item.getHeightView()));
            }
            listShadowImagesLocation.addAll(supportInstrument.getListDistanceFromParentOfItemContain());
            widthShadow = supportInstrument.getTotalWidth();
            heightShadow = supportInstrument.getTotalHeight();
            yShadowTouch = heightShadow - instrument.getHeightView() / 2;
        } else {
            widthShadow = instrument.getWidthView();
            heightShadow = instrument.getHeightView();
            yShadowTouch = heightShadow / 2;
            listShadowImagesLocation.add(new Point(0, 0));
        }
    }

    @Override
    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
        outShadowSize.set(widthShadow, heightShadow);
        outShadowTouchPoint.set(widthShadow / 2, yShadowTouch);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        int length = listShadowImages.size();
        Point supportInstrumentPoint = listShadowImagesLocation.get(0);
        canvas.translate(supportInstrumentPoint.x, supportInstrumentPoint.y);
        supportInstrumentPoint.x = 0;
        supportInstrumentPoint.y = 0;
        canvas.drawBitmap(listShadowImages.get(0), supportInstrumentPoint.x, supportInstrumentPoint.y, null);
        for (int i = 1; i < length; i++) {
            Point point = listShadowImagesLocation.get(i);
            canvas.drawBitmap(listShadowImages.get(i), supportInstrumentPoint.x + point.x, supportInstrumentPoint.y + point.y, null);
        }
    }
}
