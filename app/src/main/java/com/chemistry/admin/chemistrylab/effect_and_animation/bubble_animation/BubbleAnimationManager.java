package com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/10/2016.
 */
public class BubbleAnimationManager {
    public static int TIME_PER_LOOP = 50;
    private static final String TAG = "BAnimationSet";
    private List<BubbleAnimation> listAnimations;
    private int animationCount;
    private boolean isRunning;

    public BubbleAnimationManager(Context context) {
        listAnimations = new ArrayList<>();
        animationCount = 0;
        isRunning = false;
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(DatabaseManager.SETTINGS, Context.MODE_PRIVATE);
        double currentSpeed = Double.parseDouble(sharedPreferences.getString(DatabaseManager.KEY_SPEED, "1.0"));
        TIME_PER_LOOP -= (currentSpeed - 1) * 40;
    }

    public void addAnimation(BubbleAnimation bubbleAnimation) {
        listAnimations.add(bubbleAnimation);
        animationCount++;

        if (!isRunning) {
            new BubbleAnimationTask().start();
            isRunning = true;
        }
    }

    public void drawAllAnimation(Canvas canvas, LaboratoryHolderInstrument holder) {
        for (int i = animationCount - 1; i >= 0; i--) {
            BubbleAnimation animation = listAnimations.get(i);
            if (animation.getHolder() == holder) {
                animation.drawBubble(canvas);
            }
        }
    }

    private class BubbleAnimationTask extends Thread {
        @Override
        public void run() {
            while (animationCount > 0) {
                for (int i = animationCount - 1; i >= 0; i--) {
                    BaseAnimation animation = listAnimations.get(i);
                    if(animation.isPaused()){
                        continue;
                    }
                    if (animation.run()) {
                        animation.updateUI();
                    } else {
                        animation.getHolder().removeAnimation(animation);
                        listAnimations.remove(i);
                        animationCount--;
                    }
                }
                try {
                    Thread.sleep(TIME_PER_LOOP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isRunning = false;
        }
    }
}
