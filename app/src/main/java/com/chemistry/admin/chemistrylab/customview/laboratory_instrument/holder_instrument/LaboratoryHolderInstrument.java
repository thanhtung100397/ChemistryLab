package com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.text.SpannableString;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.chemical.BaseSubstanceManager;
import com.chemistry.admin.chemistrylab.chemical.gas.Gas;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionEquation;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.chemical.gas.GasManager;
import com.chemistry.admin.chemistrylab.chemical.liquid.Liquid;
import com.chemistry.admin.chemistrylab.chemical.liquid.LiquidManager;
import com.chemistry.admin.chemistrylab.chemical.reaction.ReactionSubstance;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;
import com.chemistry.admin.chemistrylab.chemical.solid.SolidManager;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.Fire;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.Tripod;
import com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation.BubbleAnimationManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.BaseAnimation;
import com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation.HeightChangingAnimationManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation.BoilingAnimation;
import com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation.BubbleAnimation;
import com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation.ReactionAnimation;
import com.chemistry.admin.chemistrylab.effect_and_animation.spark_animation.SparkAnimation;
import com.chemistry.admin.chemistrylab.observer.OnBoilListener;
import com.chemistry.admin.chemistrylab.observer.OnReactionListener;
import com.chemistry.admin.chemistrylab.tooltip.ToolTip;
import com.michael.easydialog.EasyDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/23/2016.
 */
public abstract class LaboratoryHolderInstrument extends LaboratoryInstrument implements View.OnDragListener,
        OnBoilListener,
        EasyDialog.OnEasyDialogDismissed {
    public static final int SOLID_MANAGER_INDEX = 0;
    public static final int LIQUID_MANAGER_INDEX = 1;
    public static final int GAS_MANAGER_INDEX = 2;
    private static final String TAG = "LaboratoryHolder";
    //Can Hold 4 levels volume - gas / liquid
    protected List<BaseSubstanceManager> listSubstanceManagers;
    protected OnReactionListener onReactionListener;
    protected HeightChangingAnimationManager heightChangingAnimationManager;
    protected BubbleAnimationManager bubbleAnimationManager;
    protected ToolTip toolTip;
    protected Paint instrumentPaint;
    protected BoilingAnimation boilingAnimation;
    protected List<BaseAnimation> listAnimation;
    private boolean isAnimationPaused = false;
    private Tripod tripodContaining;

    public LaboratoryHolderInstrument(Context context, int widthView, int heightView) {
        super(context, widthView, heightView);
        measure(MeasureSpec.makeMeasureSpec(widthView, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightView, MeasureSpec.EXACTLY));
        MainActivity mainActivity = (MainActivity) context;

        heightChangingAnimationManager = mainActivity.getHeightChangingAnimationManager();
        bubbleAnimationManager = mainActivity.getBubbleAnimationManager();

        initViews();
    }

    private void initViews() {
        listSubstanceManagers = new ArrayList<>();
        listSubstanceManagers.add(new SolidManager(getContext(), this));
        listSubstanceManagers.add(new LiquidManager(getContext(), this));
        listSubstanceManagers.add(new GasManager(getContext(), this));

        toolTip = new ToolTip(this);

        instrumentPaint = new Paint();

        listAnimation = new ArrayList<>();
    }

    public void setTripodContaining(Tripod tripodContaining) {
        this.tripodContaining = tripodContaining;
    }

    public void setOnReactionListener(OnReactionListener onReactionListener) {
        this.onReactionListener = onReactionListener;
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            manager.setOnReactionListener(onReactionListener);
        }
    }

    public void pourAllSubstancesInto(LaboratoryHolderInstrument newHolder) {
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            manager.pourAllSubstancesInto(newHolder);
        }
        newHolder.invalidate();
        invalidate();
    }

    public void checkReaction(Substance substance) {
        List<ReactionEquation> listAllEquation = new ArrayList<>();
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            if(manager.getSubstancesCount() != 0) {
                listAllEquation.addAll(manager.checkReaction(substance));
            }
        }
        setUpReaction(listAllEquation);
    }

    private void setUpReaction(List<ReactionEquation> listEquation) {
        int equationCount = listEquation.size();
        for (int i = equationCount - 1; i >= 0; i--) {
            ReactionEquation equation = listEquation.get(i);
            this.onReactionListener.reactionHappened(equation.getEquation());
            List<ReactionSubstance> listReactionSubstances = equation.getListReactionSubstances();
            int startIndex = equation.getNumberOfStartSubstance();
            int endIndex = listReactionSubstances.size();
            for (int k = startIndex; k < endIndex; k++) {
                ReactionSubstance reactionSubstance = listReactionSubstances.get(k);
                reactionSubstance.setSubstance(addSubstance(reactionSubstance.getSubstance()));
            }
            addHeightChangingAnimation(new ReactionAnimation(this, equation));
            if (equation.hasGasCreated()) {
                Substance substance = listReactionSubstances.get(0).getSubstance();
                if(substance instanceof Solid) {
                    addBubbleAnimation(new BubbleAnimation(this,
                            (Solid) substance,
                            equation.getBaseReactionSubstance().getSubstance()));
                }
            }
        }
    }

    private void addBubbleAnimation(BubbleAnimation bubbleAnimation) {
        listAnimation.add(bubbleAnimation);
        bubbleAnimationManager.addAnimation(bubbleAnimation);
    }

    private void addHeightChangingAnimation(ReactionAnimation heightChangingAnimation) {
        listAnimation.add(heightChangingAnimation);
        heightChangingAnimationManager.addAnimation(heightChangingAnimation);
    }

    public void cancelAllAnimation() {
        int animationCount = listAnimation.size();
        for (int i = animationCount - 1; i >= 0; i--) {
            listAnimation.get(i).onStop();
            listAnimation.remove(i);
        }
    }

    public void removeAnimation(BaseAnimation animation) {
        listAnimation.remove(animation);
    }

    public GasManager getGasManager() {
        return (GasManager) listSubstanceManagers.get(GAS_MANAGER_INDEX);
    }

    public SolidManager getSolidManager() {
        return (SolidManager) listSubstanceManagers.get(SOLID_MANAGER_INDEX);
    }

    public LiquidManager getLiquidManager() {
        return (LiquidManager) listSubstanceManagers.get(LIQUID_MANAGER_INDEX);
    }

    public boolean isAnimationPaused() {
        return isAnimationPaused;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(STROKE_WIDTH, STROKE_WIDTH);
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            manager.drawAllSubstances(canvas, instrumentPaint);
        }
        bubbleAnimationManager.drawAllAnimation(canvas, this);
        Path path = getInstrumentPath();
        if (path != null) {
            instrumentPaint.setStyle(Paint.Style.STROKE);
            instrumentPaint.setColor(INSTRUMENT_PATH_COLOR);
            instrumentPaint.setStrokeWidth(STROKE_WIDTH);
            canvas.drawPath(path, instrumentPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            createPath(widthView - 2 * (STROKE_WIDTH), heightView - 2 * (STROKE_WIDTH));
        }
    }

    public void createGasPath(int spaceWidth, int spaceHeight) {

    }

    @Override
    public SpannableString getName() {
        return null;
    }

    public Substance addSubstance(Substance substance) {
        Substance result = null;
        if (substance instanceof Solid) {
            result = listSubstanceManagers.get(SOLID_MANAGER_INDEX).addSubstance(substance);
        } else if (substance instanceof Liquid) {
            result = listSubstanceManagers.get(LIQUID_MANAGER_INDEX).addSubstance(substance);
        } else if (substance instanceof Gas) {
            result = listSubstanceManagers.get(GAS_MANAGER_INDEX).addSubstance(substance);
        }
        return result;
    }

    public Substance getDefaultSubstance() {
        return null;
    }

    abstract public Point[] getArrayPoint();

    abstract public int getContainedSpaceHeight();

    abstract public int getContainedSpaceWidth();

    abstract public String getTableName();

    abstract protected void createPath(int spaceWidth, int spaceHeight);

    abstract public Path getInstrumentPath();

    public ToolTip getToolTip() {
        return toolTip;
    }

    public void removeAllSubstance() {
        for (BaseSubstanceManager manager : listSubstanceManagers) {
            manager.clearAllSubstances();
        }
        toolTip.removeAllItem();
        cancelAllAnimation();
        invalidate();
    }

    public boolean isBoiling() {
        return boilingAnimation != null;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED: {

            }
            break;

            case DragEvent.ACTION_DRAG_ENTERED: {

            }
            break;

            case DragEvent.ACTION_DRAG_LOCATION: {

            }
            break;

            case DragEvent.ACTION_DRAG_EXITED: {

            }
            break;

            case DragEvent.ACTION_DROP: {
                LaboratoryInstrument dragItem = (LaboratoryInstrument) dragEvent.getLocalState();
                if (dragItem instanceof LaboratoryHolderInstrument) {
                    LaboratoryHolderInstrument holder = (LaboratoryHolderInstrument) dragItem;
                    LaboratoryHolderInstrument newHolder = (LaboratoryHolderInstrument) view;
                    holder.pourAllSubstancesInto(newHolder);
                } else if (dragItem instanceof Fire) {
                    new SparkAnimation(this).start();
                }
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
    public void onClick(View view) {
        EasyDialog toolTipView = new EasyDialog(getContext())
                .setLayout(toolTip)
                .setLocationByAttachedView(view)
                .setGravity(EasyDialog.GRAVITY_TOP)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setBackgroundColor(Color.WHITE);
        toolTip.setToolTip(toolTipView);
        toolTipView.setOnEasyDialogDismissed(this);
        toolTipView.show();
    }

    @Override
    public void onBoil() {
        boilingAnimation = new BoilingAnimation(this);
        heightChangingAnimationManager.addAnimation(boilingAnimation);
    }

    @Override
    public void onStopBoiling() {
        if (boilingAnimation != null) {
            boilingAnimation.stop();
            boilingAnimation = null;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        isAnimationPaused = visibility != VISIBLE;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(tripodContaining != null){
            tripodContaining.onItemDetached(this);
        }
        cancelAllAnimation();
    }

    @Override
    public void onDismissed() {
        ((LinearLayout) toolTip.getParent()).removeView(toolTip);
    }
}
