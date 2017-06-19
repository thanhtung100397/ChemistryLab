package com.chemistry.admin.chemistrylab.customview.laboratory_instrument;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.customview.DragItemShadowBuilder;
import com.chemistry.admin.chemistrylab.observer.OnItemDetachListener;

/**
 * Created by Admin on 8/22/2016.
 */
public abstract class LaboratoryInstrument extends View implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener {
    public static final int STROKE_WIDTH = 3;
    public static final int INSTRUMENT_PATH_COLOR = Color.BLACK;
    private static final String TAG = "LaboratoryInstrument";
    protected int widthView, heightView;
    protected static Paint instrumentPaint;
    protected OnItemDetachListener onItemDetachListener;

    public LaboratoryInstrument(Context context, int widthView, int heightView) {
        super(context);
        setLayoutParams(new RelativeLayout.LayoutParams(widthView, heightView));
        initViews();
    }

    public LaboratoryInstrument(Context context){
        super(context);
        initViews();
    }

    private void initViews() {
        if(instrumentPaint == null) {
            instrumentPaint = new Paint();
            instrumentPaint.setAntiAlias(true);
            instrumentPaint.setColor(Color.BLACK);
            instrumentPaint.setStyle(Paint.Style.STROKE);
            instrumentPaint.setStrokeWidth(STROKE_WIDTH);
        }
        setOnDragListener(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    abstract public SpannableString getName();

    abstract public LaboratoryInstrument getClone();

    public Bitmap createReviewImage(int heightSpace) {
        float ratioHeight = heightSpace / (heightView * 1.0f);
        Bitmap bitmap = Bitmap.createBitmap(Math.round(widthView * ratioHeight), heightSpace, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(ratioHeight, ratioHeight);
        this.draw(canvas);
        return bitmap;
    }

    public int getWidthView() {
        return widthView;
    }

    public int getHeightView() {
        return heightView;
    }

    public void setOnItemDetachListener(OnItemDetachListener onItemDetachListener) {
        this.onItemDetachListener = onItemDetachListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthView = MeasureSpec.getSize(widthMeasureSpec);
        heightView = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthView, heightView);
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if(dragEvent.getAction() == DragEvent.ACTION_DROP){
            LaboratoryInstrument dragItem = (LaboratoryInstrument) dragEvent.getLocalState();
            dragItem.setVisibility(VISIBLE);
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        if(onItemDetachListener != null){
            onItemDetachListener.onItemDetached((LaboratoryInstrument) view);
            onItemDetachListener = null;
        }
        DragItemShadowBuilder shadowBuilder = new DragItemShadowBuilder((LaboratoryInstrument) view);
        view.startDrag(null, shadowBuilder, view, View.DRAWING_CACHE_QUALITY_AUTO);
        ((MainActivity)getContext()).setRemoveAreaVisibility(View.VISIBLE);
        view.setVisibility(View.INVISIBLE);
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof LaboratoryInstrument) && this.getClass().equals(((LaboratoryInstrument) obj).getClass());
    }
}
