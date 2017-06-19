package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;

/**
 * Created by Admin on 9/7/2016.
 */
public class ConicalFlask extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 200;
    public static final int CONTAINED_SPACE_HEIGHT = 300;
    public static final int CONICAL_FLASK_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 2 * LaboratoryInstrument.STROKE_WIDTH;
    public static final int CONICAL_FLASK_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 2 * LaboratoryInstrument.STROKE_WIDTH;
    private static final String TAG = "ConicalFlask";
    public static final String NAME = "Bình Tam Giác";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public ConicalFlask(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
    }

    @Override
    public LaboratoryHolderInstrument getClone() {
        //No default substance, just empty
        return new ConicalFlask(getContext(), CONICAL_FLASK_STANDARD_WIDTH, CONICAL_FLASK_STANDARD_HEIGHT);
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            instrumentPath = new Path();
            int bottleNeckWidth = (int) (spaceWidth / 3.125f);//64
            int bottleNeckHeight = spaceHeight / 3;
            int roundBottomCornerDiameter = spaceWidth / 10;
            instrumentPath.moveTo((spaceWidth - bottleNeckWidth) / 2, 0);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2, bottleNeckHeight);
            instrumentPath.quadTo(0, spaceHeight - roundBottomCornerDiameter / 3, roundBottomCornerDiameter / 2, spaceHeight);
            instrumentPath.lineTo(spaceWidth - roundBottomCornerDiameter / 2, spaceHeight);
            instrumentPath.quadTo(spaceWidth, spaceHeight - roundBottomCornerDiameter / 3, (spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth, bottleNeckHeight);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth, 0);

            arrPoint = DatabaseManager.getInstance(getContext()).getArrayPointOf(DatabaseManager.FLASK_MAP_VERTICAL_TABLE_NAME);
        }
    }

    @Override
    public SpannableString getName() {
        return SpannableString.valueOf(NAME);
    }

    @Override
    public int getContainedSpaceHeight() {
        return CONTAINED_SPACE_HEIGHT;
    }

    @Override
    public int getContainedSpaceWidth() {
        return CONTAINED_SPACE_WIDTH;
    }

    @Override
    public String getTableName() {
        return DatabaseManager.CONICAL_FLASK_MAP_HORIZONTAL_TABLE_NAME;
    }

    @Override
    public Point[] getArrayPoint() {
        return arrPoint;
    }

    @Override
    public Path getInstrumentPath() {
        return instrumentPath;
    }
}
