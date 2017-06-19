package com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/5/2016.
 */
public class HeightChangingAnimationManager {
    private static final String TAG = "HCAnimationSet";
    public static int TIME_PER_LOOP = 1000;
    private List<BaseAnimation> listAnimations;
    private int animationCount;
    private boolean isRunning;

    public HeightChangingAnimationManager(Context context) {
        this.listAnimations = new ArrayList<>();
        this.animationCount = 0;
        isRunning = false;
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(DatabaseManager.SETTINGS, Context.MODE_PRIVATE);
        double currentSpeed = Double.parseDouble(sharedPreferences.getString(DatabaseManager.KEY_SPEED, "1.0"));
        TIME_PER_LOOP -= (currentSpeed - 1) * 900;
    }

    public void addAnimation(BaseAnimation animation) {
        listAnimations.add(animation);
        animationCount++;

        if (!isRunning) {
            new HeightChangingAnimationTask().execute();
            isRunning = true;
        }
    }

    private class HeightChangingAnimationTask extends AsyncTask<Void, BaseAnimation, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (animationCount > 0) {
                for (int i = animationCount - 1; i >= 0; i--) {
                    BaseAnimation animation = listAnimations.get(i);
                    if(animation.isPaused()){
                        continue;
                    }
                    if (!animation.run()) {
                        animation.getHolder().removeAnimation(animation);
                        listAnimations.remove(i);
                        animationCount--;
                    }
                    publishProgress(animation);
                }
                SystemClock.sleep(TIME_PER_LOOP);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(BaseAnimation... animations) {
            animations[0].updateUI();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            isRunning = false;
        }
    }
}
