package com.chemistry.admin.chemistrylab.customview.laboratory_instrument;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.view.View;
import android.widget.RelativeLayout;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.observer.OnBoilListener;

/**
 * Created by Admin on 9/8/2016.
 */
public class AlcoholBurner extends LaboratoryInstrument {
    public static final String NAME = "Đèn Cồn";
    private static final String TAG = "Burner";
    private static int flameImageWidth;
    private static Bitmap burnerOpenedImage;
    private static Bitmap burnerClosedImage;
    private static Bitmap flameState[];
    private Bitmap currentState;
    private boolean isBurning;
    private int flameStateIndex;
    private int i;
    private OnBoilListener onBoilListener;

    public AlcoholBurner(Context context) {
        super(context);
        if (burnerClosedImage == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            burnerOpenedImage = BitmapFactory.decodeResource(getResources(), R.drawable.burner_opened, options);
            burnerClosedImage = BitmapFactory.decodeResource(getResources(), R.drawable.burner_closed, options);
            createFlameState();
        }

        isBurning = false;

        currentState = burnerClosedImage;
        widthView = burnerOpenedImage.getWidth();
        heightView = burnerOpenedImage.getHeight() + 70;
        setLayoutParams(new RelativeLayout.LayoutParams(widthView, heightView));
        measure(MeasureSpec.makeMeasureSpec(widthView, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightView, MeasureSpec.EXACTLY));
    }

    private void createFlameState() {
        flameState = new Bitmap[8];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Resources resources = getResources();
        flameState[0] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_1, options);
        flameImageWidth = flameState[0].getWidth();
        flameState[1] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_2, options);
        flameState[2] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_3, options);
        flameState[3] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_4, options);
        flameState[4] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_5, options);
        flameState[5] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_6, options);
        flameState[6] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_7, options);
        flameState[7] = BitmapFactory.decodeResource(resources, R.drawable.ic_flame_burner_8, options);

        flameStateIndex = 0;
    }

    public void setOnBoilListener(OnBoilListener onBoilListener) {
        this.onBoilListener = onBoilListener;
        if(isBurning){
            onBoilListener.onBoil();
        }
    }

    public void removeOnBoilListener(){
        this.onBoilListener = null;
    }

    public void stopBoiling(){
        if(onBoilListener != null) {
            this.onBoilListener.onStopBoiling();
        }
    }

    @Override
    public AlcoholBurner getClone() {
        return new AlcoholBurner(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isBurning) {
            canvas.drawBitmap(burnerOpenedImage, 0, 70, null);
            if(flameStateIndex == 7) {
                i = -1;
            }
            if(flameStateIndex == 0){
                i = 1;
            }
            flameStateIndex+=i;
            canvas.drawBitmap(flameState[flameStateIndex], (widthView - flameImageWidth + 4) / 2, 0, null);
            postInvalidateDelayed(100);
        } else {
            canvas.drawBitmap(burnerClosedImage, 0, 70, null);
        }
    }

    @Override
    public SpannableString getName() {
        return SpannableString.valueOf(NAME);
    }

    @Override
    public Bitmap createReviewImage(int heightSpace) {
        float ratio = heightSpace / (heightView * 1.0f);
        if (ratio == 1.0f) {
            return currentState;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Math.round(1 / ratio);
        return BitmapFactory.decodeResource(getResources(), R.drawable.burner_closed, options);
    }

    @Override
    public void onClick(View view) {
        if (isBurning) {
            currentState = burnerClosedImage;
            isBurning = false;
            stopBoiling();
        } else {
            currentState = burnerOpenedImage;
            isBurning = true;
            if(onBoilListener != null) {
                onBoilListener.onBoil();
            }
        }
        invalidate();
    }

    @Override
    public boolean onLongClick(View view) {
        stopBoiling();
        return super.onLongClick(view);
    }
}
