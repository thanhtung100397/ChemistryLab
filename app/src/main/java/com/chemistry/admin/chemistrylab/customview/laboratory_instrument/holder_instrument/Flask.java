package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;

/**
 * Created by Admin on 9/6/2016.
 */
public class Flask extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 200;
    public static final int CONTAINED_SPACE_HEIGHT = 300;
    public static final int FLASK_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 2 * LaboratoryInstrument.STROKE_WIDTH;
    public static final int FLASK_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 2 * LaboratoryInstrument.STROKE_WIDTH;
    private static final String TAG = "Flask";
    public static final String NAME = "Bình Cầu";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public Flask(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
    }

    @Override
    public LaboratoryHolderInstrument getClone() {
        //No default substance, just empty
        return new Flask(getContext(), FLASK_STANDARD_WIDTH, FLASK_STANDARD_HEIGHT);
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            instrumentPath = new Path();
            float bottleNeckWidth = spaceWidth / 3.125f;//64
            float bottleNeckHeight = spaceHeight / 3;//>100
            float angle = (float) Math.toDegrees(Math.acos(bottleNeckWidth / spaceWidth));
            float startAngle = 180 + angle;
            float sweepAngle = -(startAngle + angle);
            instrumentPath.moveTo((spaceWidth - bottleNeckWidth) / 2, 0);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2, bottleNeckHeight);
            instrumentPath.arcTo(new RectF(0, bottleNeckHeight, spaceWidth, spaceHeight),
                                startAngle, sweepAngle);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth, 0);

            arrPoint = DatabaseManager.getInstance(getContext())
                    .getArrayPointOf(DatabaseManager.FLASK_MAP_VERTICAL_TABLE_NAME);
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
        return DatabaseManager.FLASK_MAP_HORIZONTAL_TABLE_NAME;
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
