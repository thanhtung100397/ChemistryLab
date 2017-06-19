package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.SpannableString;

import com.chemistry.admin.chemistrylab.chemical.BaseSubstanceManager;
import com.chemistry.admin.chemistrylab.chemical.gas.Gas;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.util.PointAnalyzer;

/**
 * Created by Admin on 8/23/2016.
 */
public class GasBottle extends LaboratoryHolderInstrument {
    public static final int CONTAINED_SPACE_WIDTH = 200;
    public static final int CONTAINED_SPACE_HEIGHT = 300;
    public static final int GAS_BOTTLE_STANDARD_WIDTH = CONTAINED_SPACE_WIDTH + 2 * LaboratoryInstrument.STROKE_WIDTH;
    public static final int GAS_BOTTLE_STANDARD_HEIGHT = CONTAINED_SPACE_HEIGHT + 10 + 2 * LaboratoryInstrument.STROKE_WIDTH;
    private static final String TAG = "GasBottle";
    public static final String NAME = "Bình Chứa Khí";
    private static Point arrPoint[];
    private static Path instrumentPath;

    public GasBottle(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
        createGasPath(GAS_BOTTLE_STANDARD_WIDTH - 2 * STROKE_WIDTH, GAS_BOTTLE_STANDARD_HEIGHT - 2 * STROKE_WIDTH);
    }

    @Override
    public GasBottle getClone() {
        GasBottle copy = new GasBottle(getContext(), GAS_BOTTLE_STANDARD_WIDTH, GAS_BOTTLE_STANDARD_HEIGHT);
        if (getDefaultSubstance() != null) {
            copy.addSubstance(getDefaultSubstance().getClone());
        }
        return copy;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(STROKE_WIDTH, STROKE_WIDTH + 10);
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            manager.drawAllSubstances(canvas, instrumentPaint);
        }
        bubbleAnimationManager.drawAllAnimation(canvas, this);
        canvas.translate(0, -10);
        Path path = getInstrumentPath();
        if (path != null) {
            instrumentPaint.setStyle(Paint.Style.STROKE);
            instrumentPaint.setColor(INSTRUMENT_PATH_COLOR);
            instrumentPaint.setStrokeWidth(STROKE_WIDTH);
            canvas.drawPath(path, instrumentPaint);
        }
    }

    @Override
    protected void createPath(int spaceWidth, int spaceHeight) {
        if (instrumentPath == null) {
            instrumentPath = new Path();
            int stopperWidth = spaceWidth / 4;
            int stopperHeight = spaceWidth / 30;
            int bottleNeckWidth = spaceWidth / 10;
            int bottleNeckHeight = (spaceHeight - 10) / 20;
            int roundTopCornerDiameter = spaceWidth / 2;

            instrumentPath.moveTo((spaceWidth - bottleNeckWidth) / 2,
                    stopperHeight);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2,
                    stopperHeight + bottleNeckHeight);
            instrumentPath.arcTo(new RectF(0,
                            stopperHeight + bottleNeckHeight,
                            roundTopCornerDiameter,
                            stopperHeight + bottleNeckHeight + roundTopCornerDiameter),
                    270, -90);
            instrumentPath.lineTo(0, spaceHeight);
            instrumentPath.lineTo(spaceWidth, spaceHeight);
            instrumentPath.arcTo(new RectF(roundTopCornerDiameter,
                            stopperHeight + bottleNeckHeight,
                            roundTopCornerDiameter * 2,
                            stopperHeight + bottleNeckHeight + roundTopCornerDiameter),
                    0, -90);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth,
                    stopperHeight + bottleNeckHeight);
            instrumentPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth,
                    stopperHeight);

//          DatabaseManager.getInstance(getContext()).insertToDataBase(DatabaseManager.GAS_BOTTLE_MAP_VERTICAL_TABLE_NAME,PointAnalyzer.analyzePointVertical(instrumentPath));
            arrPoint = DatabaseManager.getInstance(getContext())
                    .getArrayPointOf(DatabaseManager.GAS_BOTTLE_MAP_VERTICAL_TABLE_NAME);

            instrumentPath.addRect(new RectF((spaceWidth - stopperWidth) / 2,
                            0,
                            (spaceWidth - stopperWidth) / 2 + stopperWidth,
                            stopperHeight),
                    Path.Direction.CW);
        }
    }

    @Override
    public void createGasPath(int spaceWidth, int spaceHeight) {
        if (getGasManager().getGasPath() == null) {
            float stopperHeight = spaceWidth / 30;
            float bottleNeckWidth = spaceWidth / 10;
            float bottleNeckHeight = (spaceHeight - 10) / 20;
            float roundTopCornerDiameter = spaceWidth / 2;

            Path gasPath = new Path();
            gasPath.moveTo((spaceWidth - bottleNeckWidth) / 2, stopperHeight);
            gasPath.lineTo((spaceWidth - bottleNeckWidth) / 2, stopperHeight + bottleNeckHeight);
            gasPath.arcTo(new RectF(0,
                            stopperHeight + bottleNeckHeight,
                            0 + roundTopCornerDiameter,
                            stopperHeight + bottleNeckHeight + roundTopCornerDiameter),
                    270, -90);
            gasPath.lineTo(0, spaceHeight);
            gasPath.lineTo(spaceWidth, spaceHeight);
            gasPath.arcTo(new RectF(0 + roundTopCornerDiameter,
                            stopperHeight + bottleNeckHeight,
                            roundTopCornerDiameter * 2,
                            stopperHeight + bottleNeckHeight + roundTopCornerDiameter),
                    0, -90);
            gasPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth,
                    stopperHeight + bottleNeckHeight);
            gasPath.lineTo((spaceWidth - bottleNeckWidth) / 2 + bottleNeckWidth, stopperHeight);
            gasPath.lineTo((spaceWidth - bottleNeckWidth) / 2, stopperHeight);

            getGasManager().setGasPath(gasPath);
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
    public Gas getDefaultSubstance() {
        return getGasManager().getSubstance(0);
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
        return DatabaseManager.GAS_BOTTLE_MAP_HORIZONTAL_TABLE_NAME;
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
