package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.database.DatabaseManager;

/**
 * Created by Admin on 9/6/2016.
 */
public class TestTube extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 50;
    public static final int CONTAINED_SPACE_HEIGHT = 300;
    public static final int TEST_TUBE_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 2 * STROKE_WIDTH;
    public static final int TEST_TUBE_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 2 * STROKE_WIDTH;
    private static final String TAG = "TestTube";
    public static final String NAME = "Ống Nghiệm";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public TestTube(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
    }

    @Override
    public LaboratoryHolderInstrument getClone() {
        //No default substance, just empty
        return new TestTube(getContext(), TEST_TUBE_STANDARD_WIDTH, TEST_TUBE_STANDARD_HEIGHT);
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            instrumentPath = new Path();
            instrumentPath.moveTo(0, 0);
            instrumentPath.lineTo(0, spaceHeight - spaceWidth);
            instrumentPath.arcTo(new RectF(0,
                                            spaceHeight - spaceWidth,
                                            spaceWidth,
                                            spaceHeight),
                                180, -180);
            instrumentPath.lineTo(spaceWidth, 0);

            arrPoint = DatabaseManager.getInstance(getContext())
                    .getArrayPointOf(DatabaseManager.TEST_TUBE_MAP_VERTICAL_TABLE_NAME);
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
        return DatabaseManager.TEST_TUBE_MAP_HORIZONTAL_TABLE_NAME;
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
