package com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Admin on 8/28/2016.
 */
public class Bubble {
    public static final int BUBBLE_SIZE = 5;
    public static Bitmap smallBubbleImage;
    public static Bitmap bigBubbleImage;
    private int x, y;
    private int yEnd;
    private int speed;
    private int yMin;
    private Bitmap bubble;


    public Bubble(int x, int y, int yMin, int speed, boolean isSmall) {
        this.x = x;
        this.y = y;
        this.yMin = yMin;
        this.speed = speed;
        if(isSmall){
            bubble = smallBubbleImage;
        }else {
            bubble = bigBubbleImage;
        }
    }

    public void drawBubble(Canvas canvas) {
        canvas.drawBitmap(bubble, x, y, null);
    }

    public void setYEnd(int yEnd) {
        this.yEnd = yEnd;
    }

    public int getYMin() {
        return yMin;
    }

    public boolean move(){
        if(this.y >= yEnd) {
            this.y -= speed;
            return true;
        }
        return false;
    }
}
