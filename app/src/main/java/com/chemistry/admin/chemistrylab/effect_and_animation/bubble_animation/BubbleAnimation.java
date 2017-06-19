package com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.chemical.liquid.LiquidManager;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 8/28/2016.
 */
public class BubbleAnimation implements BaseAnimation {
    public static final int BUBBLE_LIMIT_COUNT = 20;
    public static final int BUBBLE_SPEED_MAX = 3;
    public static final int BUBBLE_SPEED_MIN = 1;
    private static final String TAG = "BubbleAnimation";
    private List<Bubble> listBubble;
    private int bubbleCount;
    private Random random;
    private String holderTableName;
    private Context context;
    private Solid solid;
    private LiquidManager liquidManager;
    private Substance baseSubstance;
    private LaboratoryHolderInstrument holder;

    public BubbleAnimation(LaboratoryHolderInstrument holder, Solid solid, Substance baseSubstance) {
        this.holder = holder;
        this.context = holder.getContext();
        if (Bubble.smallBubbleImage == null) {
            Bubble.smallBubbleImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bubble_small);
            Bubble.bigBubbleImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bubble_big);
        }
        this.random = new Random();
        this.listBubble = new ArrayList<>();
        this.bubbleCount = 0;
        this.holderTableName = holder.getTableName();
        this.liquidManager = holder.getLiquidManager();
        this.solid = solid;
        this.baseSubstance = baseSubstance;
        createABubble();
    }

    @Override
    public boolean run() {
        if (bubbleCount == 0 && baseSubstance.getMole() == 0) {
            return false;
        }
        for (int i = bubbleCount - 1; i >= 0; i--) {
            Bubble bubble = listBubble.get(i);
            if (bubble.move()) {
                reCreateYEnd(bubble);
            } else {
                listBubble.remove(i);
                bubbleCount--;
            }
            if (baseSubstance.getMole() != 0 && bubbleCount < BUBBLE_LIMIT_COUNT) {
                if (random.nextInt(10) == 0) {
                    createABubble();
                }
            }
        }
        return true;
    }

    @Override
    public void updateUI() {
        holder.postInvalidate();
    }

    public void drawBubble(Canvas canvas) {
        for (int i = listBubble.size() - 1; i >= 0; i--) {
            Bubble bubble = listBubble.get(i);
            if(bubble != null) {
                bubble.drawBubble(canvas);
            }
        }
    }

    @Override
    public void onStop() {
        bubbleCount = 0;
    }

    private void createABubble() {
        Point surfaceLine = solid.getSurfaceLine();
        int xBubble = random.nextInt(surfaceLine.y - Bubble.BUBBLE_SIZE - surfaceLine.x) + surfaceLine.x;
        int yBubble = solid.getYTop();
        int yEnd;
        int yBubbleMax = DatabaseManager.getInstance(context).getYByX(holderTableName, xBubble);
        int holderEmptyHeight = (int) liquidManager.getEmptyHeight();
        if (yBubbleMax <= holderEmptyHeight) {
            yEnd = holderEmptyHeight;
        } else {
            yEnd = yBubbleMax;
        }
        int speed = random.nextInt(BUBBLE_SPEED_MAX) + BUBBLE_SPEED_MIN;
        boolean isSmall =  random.nextInt(2) == 0;
        Bubble bubble = new Bubble(xBubble, yBubble, yBubbleMax, speed, isSmall);
        bubble.setYEnd(yEnd);
        listBubble.add(bubble);
        bubbleCount++;
    }

    private void reCreateYEnd(Bubble bubble) {
        int yEnd = (int) liquidManager.getEmptyHeight();
        int yMin = bubble.getYMin();
        if (yMin <= yEnd) {
            bubble.setYEnd(yEnd);
        } else {
            bubble.setYEnd(yMin);
        }
    }

    @Override
    public boolean isPaused() {
        return holder.isAnimationPaused();
    }

    @Override
    public LaboratoryHolderInstrument getHolder() {
        return holder;
    }
}
