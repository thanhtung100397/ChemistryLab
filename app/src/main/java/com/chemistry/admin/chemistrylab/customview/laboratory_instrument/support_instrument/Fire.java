package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.SpannableString;
import android.view.View;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;

/**
 * Created by Admin on 10/18/2016.
 */

public class Fire extends LaboratoryInstrument {
    private static final String TAG = "Fire";
    public static final String NAME = "Lửa";

    private static Bitmap fireState[];
    private int fireStateIndex;
    private int i;

    public Fire(Context context) {
        super(context);
        if (fireState == null) {
            initFireState();
        }
        this.widthView = fireState[0].getWidth();
        this.heightView = fireState[0].getHeight();
    }

    private void initFireState() {
        Resources resources = getContext().getResources();
        fireState = new Bitmap[9];
        fireState[0] = BitmapFactory.decodeResource(resources, R.drawable.fire_1);
        fireState[1] = BitmapFactory.decodeResource(resources, R.drawable.fire_2);
        fireState[2] = BitmapFactory.decodeResource(resources, R.drawable.fire_3);
        fireState[3] = BitmapFactory.decodeResource(resources, R.drawable.fire_4);
        fireState[4] = BitmapFactory.decodeResource(resources, R.drawable.fire_5);
        fireState[5] = BitmapFactory.decodeResource(resources, R.drawable.fire_6);
        fireState[6] = BitmapFactory.decodeResource(resources, R.drawable.fire_7);
        fireState[7] = BitmapFactory.decodeResource(resources, R.drawable.fire_8);
        fireState[8] = BitmapFactory.decodeResource(resources, R.drawable.fire_9);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (fireStateIndex == 8) {
            i = -1;
        }
        if (fireStateIndex == 0) {
            i = 1;
        }
        fireStateIndex += i;
        canvas.drawBitmap(fireState[fireStateIndex], 0, 0, null);
        postInvalidateDelayed(70);
    }

    @Override
    public SpannableString getName() {
        return SpannableString.valueOf(NAME);
    }

    @Override
    public LaboratoryInstrument getClone() {
        return new Fire(getContext());
    }

    @Override
    public void onClick(View v) {

    }
}
