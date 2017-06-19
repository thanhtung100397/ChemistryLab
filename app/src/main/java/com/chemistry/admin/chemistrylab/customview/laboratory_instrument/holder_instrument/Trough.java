package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;

/**
 * Created by Admin on 9/7/2016.
 */
public class Trough extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 400;
    public static final int CONTAINED_SPACE_HEIGHT = 200;
    public static final int TROUGH_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 2 * LaboratoryInstrument.STROKE_WIDTH;
    public static final int TROUGH_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 2 * LaboratoryInstrument.STROKE_WIDTH;
    private static final String TAG = "Trough";
    public static final String NAME = "Chậu";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public Trough(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
    }

    @Override
    public Trough getClone() {
        return new Trough(getContext(), TROUGH_STANDARD_WIDTH, TROUGH_STANDARD_HEIGHT);
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            int roundBottomCornerDiameter = spaceHeight / 5;
            instrumentPath = new Path();
            instrumentPath.moveTo(0, 0);
            instrumentPath.lineTo(0, spaceHeight - roundBottomCornerDiameter);
            instrumentPath.arcTo(new RectF(0,
                                            spaceHeight - roundBottomCornerDiameter,
                                            roundBottomCornerDiameter,
                                            spaceHeight - roundBottomCornerDiameter + roundBottomCornerDiameter),
                                180, -90);
            instrumentPath.arcTo(new RectF(spaceWidth - roundBottomCornerDiameter,
                                            spaceHeight - roundBottomCornerDiameter,
                                            spaceWidth - roundBottomCornerDiameter + roundBottomCornerDiameter,
                                            spaceHeight - roundBottomCornerDiameter + roundBottomCornerDiameter),
                                90, -90);
            instrumentPath.lineTo(spaceWidth, 0);

//          DatabaseManager.getInstance(getContext()).insertToDataBase(DatabaseManager.BREAKER_MAP_VERTICAL_TABLE_NAME,PointAnalyzer.analyzePointVertical(instrumentPath));
            arrPoint = DatabaseManager.getInstance(getContext())
                    .getArrayPointOf(DatabaseManager.TROUGH_MAP_VERTICAL_TABLE_NAME);
        }
    }

    @Override
    public SpannableString getName() {
        if (getDefaultSubstance() != null) {
            return getDefaultSubstance().getConvertSymbol();
        }
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
        return DatabaseManager.TROUGH_MAP_HORIZONTAL_TABLE_NAME;
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
