package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.text.SpannableString;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.AlcoholBurner;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;

/**
 * Created by Admin on 9/8/2016.
 */
public class Tripod extends LaboratorySupportInstrument implements View.OnDragListener {
    private static final String TAG = "Tripod";
    public static final String NAME = "Kiềng";
    private static Bitmap tripodImage;
    private AlcoholBurner alcoholBurner;
    private LaboratoryHolderInstrument holder;

    public Tripod(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        tripodImage = BitmapFactory.decodeResource(getResources(), R.drawable.tripod, options);
        widthView = tripodImage.getWidth();
        heightView = tripodImage.getHeight();
        totalWidth = widthView;
        totalHeight = heightView;

        setLayoutParams(new RelativeLayout.LayoutParams(widthView, heightView));
        measure(MeasureSpec.makeMeasureSpec(widthView, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightView, MeasureSpec.EXACTLY));
    }

    @Override
    public void addItem(LaboratoryInstrument item) {
        if (item instanceof LaboratoryHolderInstrument && !listItemContain.contains(item)) {
            if (item.getParent() == null) {
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.getMainLayout().addView(item);
                ((LaboratoryHolderInstrument) item).setOnReactionListener(mainActivity);
            }
            listItemContain.add(item);
            item.setOnItemDetachListener(this);
            int distanceX = (this.widthView - item.getWidthView()) / 2;
            int distanceY = -item.getHeightView();
            calculateTotalDimension(distanceX, distanceY);
            listDistanceFromParentOfItemContain.add(new Point(distanceX, distanceY));
            item.setX(this.getX() + distanceX);
            item.setY(this.getY() + distanceY);
            this.holder = (LaboratoryHolderInstrument) item;
            this.holder.setTripodContaining(this);
        } else if (item instanceof AlcoholBurner && !listItemContain.contains(item)) {
            if (item.getParent() == null) {
                ((MainActivity) getContext()).getMainLayout().addView(item);
            }
            listItemContain.add(item);
            item.setOnItemDetachListener(this);
            int distanceX = (this.widthView - item.getWidthView()) / 2;
            int distanceY = this.heightView - item.getHeightView();
            listDistanceFromParentOfItemContain.add(new Point(distanceX, distanceY));
            item.setX(this.getX() + distanceX);
            item.setY(this.getY() + distanceY);
            this.alcoholBurner = (AlcoholBurner) item;
        }

        if(holder != null && alcoholBurner != null){
            alcoholBurner.setOnBoilListener(holder);
        }

    }

    @Override
    protected void calculateTotalDimension(int distanceX, int distanceY) {
        if (distanceX < 0) {
            totalWidth -= distanceX;
            xInGroup -= distanceX;
        }
        totalHeight -= distanceY;
        yInGroup -= distanceY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(tripodImage, 0, 0, null);
    }

    @Override
    public Tripod getClone() {
        return new Tripod(getContext());
    }

    @Override
    public SpannableString getName() {
        return SpannableString.valueOf(NAME);
    }

    @Override
    public Bitmap createReviewImage(int heightSpace) {
        float ratio = heightSpace / (heightView * 1.0f);
        if (ratio == 1.0f) {
            return tripodImage;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Math.round(1 / ratio);
        return BitmapFactory.decodeResource(getResources(), R.drawable.tripod, options);
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED: {

            }
            break;

            case DragEvent.ACTION_DRAG_ENTERED: {
                view.setScaleX(1.1f);
                view.setScaleY(1.1f);
            }
            break;

            case DragEvent.ACTION_DRAG_LOCATION: {

            }
            break;

            case DragEvent.ACTION_DRAG_EXITED: {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            }
            break;

            case DragEvent.ACTION_DROP: {
                LaboratoryInstrument dragItem = (LaboratoryInstrument) dragEvent.getLocalState();
                addItem(dragItem);
                dragItem.requestLayout();
                dragItem.bringToFront();//call this to make item is in font of this tripod, not behind;
                ((MainActivity) getContext()).getMainLayout().invalidate();
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                dragItem.setVisibility(VISIBLE);
            }
            break;

            case DragEvent.ACTION_DRAG_ENDED: {

            }
            break;

            default: {
                break;
            }
        }
        return true;
    }

    @Override
    public void onItemDetached(LaboratoryInstrument item) {
        if (item instanceof LaboratoryHolderInstrument) {
            totalWidth = widthView;
            totalHeight = heightView;
            xInGroup = 0;
            yInGroup = 0;
            if(alcoholBurner != null) {
                alcoholBurner.stopBoiling();
                alcoholBurner.removeOnBoilListener();
            }
        }
        super.onItemDetached(item);
    }
}
