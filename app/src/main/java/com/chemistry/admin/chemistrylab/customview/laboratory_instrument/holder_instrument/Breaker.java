package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.chemical.liquid.Liquid;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;

/**
 * Created by Admin on 8/22/2016.
 */
public class Breaker extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 200;
    public static final int CONTAINED_SPACE_HEIGHT = 300;
    public static final int BREAKER_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 10 + 2 * STROKE_WIDTH;
    public static final int BREAKER_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 2 * STROKE_WIDTH;
    private static final String TAG = "Breaker";
    public static final String NAME = "Cốc Trụ";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public Breaker(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
    }

    @Override
    public Breaker getClone() {
        Breaker copy = new Breaker(getContext(), BREAKER_STANDARD_WIDTH, BREAKER_STANDARD_HEIGHT);
        if (getDefaultSubstance() != null) {
            copy.addSubstance(getDefaultSubstance().getClone());
        }
        return copy;
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            int roundTopCornerDiameter = (spaceWidth - 10) / 10;
            int roundBottomCornerDiameter = (spaceWidth - 10) / 5;
            instrumentPath = new Path();
            instrumentPath.moveTo(0, 0);
            instrumentPath.arcTo(new RectF(-roundTopCornerDiameter / 2,
                            0,
                            -roundTopCornerDiameter / 2 + roundTopCornerDiameter,
                            0 + roundTopCornerDiameter),
                    270, 90);
            instrumentPath.arcTo(new RectF(roundTopCornerDiameter / 2,
                            spaceHeight - roundBottomCornerDiameter,
                            roundTopCornerDiameter / 2 + roundBottomCornerDiameter,
                            spaceHeight - roundBottomCornerDiameter + roundBottomCornerDiameter),
                    180, -90);
            instrumentPath.arcTo(new RectF(spaceWidth - roundBottomCornerDiameter,
                            spaceHeight - roundBottomCornerDiameter,
                            spaceWidth - roundBottomCornerDiameter + roundBottomCornerDiameter,
                            spaceHeight - roundBottomCornerDiameter + roundBottomCornerDiameter),
                    90, -90);
            instrumentPath.lineTo(spaceWidth, 0);

//            DatabaseManager.getInstance(getContext()).insertToDataBase(DatabaseManager.BREAKER_MAP_VERTICAL_TABLE_NAME,PointAnalyzer.analyzePointVertical(instrumentPath));
            arrPoint = DatabaseManager.getInstance(getContext())
                    .getArrayPointOf(DatabaseManager.BREAKER_MAP_VERTICAL_TABLE_NAME);
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
    public Liquid getDefaultSubstance() {
        return getLiquidManager().getSubstance(0);
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
        return DatabaseManager.BREAKER_MAP_HORIZONTAL_TABLE_NAME;
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
