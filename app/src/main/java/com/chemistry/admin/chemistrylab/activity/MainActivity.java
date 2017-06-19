package com.chemistry.admin.chemistrylab.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.LaboratorySupportInstrument;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.dialog.SettingsDialog;
import com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation.BubbleAnimationManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation.HeightChangingAnimationManager;
import com.chemistry.admin.chemistrylab.fragment.SearchFragment;
import com.chemistry.admin.chemistrylab.observer.OnReactionListener;
import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.fragment.LaboratoryFragment;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, View.OnDragListener, OnReactionListener {
    private static final String TAG = "MainActivity";
    private RelativeLayout mainLayout;
    private RelativeLayout menuLayout;
    private LinearLayout layoutMenuButton;
    private Button buttonMenu, buttonLaboratory, buttonSearch, buttonDocument, buttonSettings;
    private ImageView removeArea;

    private LaboratoryFragment laboratoryFragment;
    private SearchFragment searchFragment;
    private Fragment currentFragment;
    private Fragment previousFragment;

    private Animator.AnimatorListener buttonMenuAnimatorListener,
            layoutMenuButtonAnimatorListener,
            menuLayoutAnimatorListener,
            equationAnimatorListener;
    private Animation.AnimationListener itemRemovingAnimationListener;
    private int mainLayoutWidth, mainLayoutHeight;
    private TextView equationsView;
    private LaboratoryInstrument dragItem;
    private AnimationSet itemRemovingAnimation;
    private HeightChangingAnimationManager heightChangingAnimationManager;
    private BubbleAnimationManager bubbleAnimationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        DatabaseManager.getInstance(this);//Copy data to database
        buttonMenu = (Button) findViewById(R.id.btn_menu);
        buttonMenu.setOnClickListener(this);

        layoutMenuButton = (LinearLayout) findViewById(R.id.ln_button_menu);

        mainLayout = (RelativeLayout) findViewById(R.id.rl_main);
        mainLayout.setOnDragListener(this);
        mainLayout.setOnClickListener(this);

        menuLayout = (RelativeLayout) findViewById(R.id.rl_menu);

        final ViewTreeObserver viewTreeObserver = mainLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayoutWidth = mainLayout.getMeasuredWidth();
                mainLayoutHeight = mainLayout.getMeasuredHeight();
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        equationsView = (TextView) findViewById(R.id.txt_equation);

        buttonLaboratory = (Button) findViewById(R.id.btn_laboratory);
        buttonSearch = (Button) findViewById(R.id.btn_search);
        buttonDocument = (Button) findViewById(R.id.btn_document);
        buttonSettings = (Button) findViewById(R.id.btn_settings);
        buttonLaboratory.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonDocument.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);

        removeArea = (ImageView) findViewById(R.id.img_remove);
        removeArea.setOnDragListener(this);

        createItemRemovingAnimation();
        initAnimatorListener();

        heightChangingAnimationManager = new HeightChangingAnimationManager(this);
        bubbleAnimationManager = new BubbleAnimationManager(this);

        initFragment();
    }

    private void initFragment() {
        laboratoryFragment = new LaboratoryFragment();
        laboratoryFragment.setmButton(buttonLaboratory);
        searchFragment = new SearchFragment();
        searchFragment.setmButton(buttonSearch);
    }

    private void initAnimatorListener() {
        buttonMenuAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonMenu.setVisibility(View.INVISIBLE);
                layoutMenuButton.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(layoutMenuButton);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        layoutMenuButtonAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layoutMenuButton.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.FadeIn).duration(300).playOn(buttonMenu);
                buttonMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        menuLayoutAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (previousFragment != null) {
                    getFragmentManager().beginTransaction()
                            .remove(previousFragment)
                            .commit();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        itemRemovingAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (dragItem instanceof LaboratorySupportInstrument) {
                            List<LaboratoryInstrument> listItemContain = ((LaboratorySupportInstrument) dragItem).getListItemContain();
                            for (LaboratoryInstrument instrument : listItemContain) {
                                mainLayout.removeView(instrument);
                            }
                        }
                        mainLayout.removeView(dragItem);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        equationAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

    }

    private void createItemRemovingAnimation() {
        removeArea.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, removeArea.getX() + removeArea.getWidth() / 2, removeArea.getY() + removeArea.getHeight() / 2);
                rotateAnimation.setDuration(300);
                rotateAnimation.setRepeatCount(2);
                AlphaAnimation fadeAnimation = new AlphaAnimation(1, 0);
                fadeAnimation.setDuration(600);
                itemRemovingAnimation = new AnimationSet(false);
                itemRemovingAnimation.addAnimation(rotateAnimation);
                itemRemovingAnimation.addAnimation(fadeAnimation);
                itemRemovingAnimation.setDuration(600);
                itemRemovingAnimation.setAnimationListener(itemRemovingAnimationListener);
                removeArea.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void showMenuFragment(Fragment fragment) {
        currentFragment = fragment;
        getFragmentManager().beginTransaction()
                .replace(R.id.rl_menu, fragment)
                .commit();

        YoYo.with(Techniques.SlideInLeft)
                .duration(400)
                .playOn(menuLayout);
    }

    private void closeMenuFragment() {
        previousFragment = currentFragment;
        currentFragment = null;
        YoYo.with(Techniques.SlideOutLeft)
                .withListener(menuLayoutAnimatorListener)
                .duration(400)
                .playOn(menuLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu: {
                YoYo.with(Techniques.FadeOut).withListener(buttonMenuAnimatorListener).duration(100).playOn(view);
            }
            break;

            case R.id.btn_laboratory: {
                Drawable background = view.getBackground();
                if (background.getLevel() == 0) {
                    showMenuFragment(laboratoryFragment);
                    background.setLevel(1);
                } else {
                    closeMenuFragment();
                    background.setLevel(0);
                }
            }
            break;

            case R.id.btn_search: {
                Drawable background = view.getBackground();
                if (background.getLevel() == 0) {
                    showMenuFragment(searchFragment);
                    background.setLevel(1);
                } else {
                    closeMenuFragment();
                    background.setLevel(0);
                }
            }
            break;

            case R.id.btn_document: {
                startActivity(new Intent(this, DocumentActivity.class));
            }
            break;

            case R.id.btn_settings: {
                new SettingsDialog(this).show();
            }
            break;

            case R.id.rl_main: {
                if (buttonMenu.getVisibility() == View.INVISIBLE) {
                    YoYo.with(Techniques.SlideOutLeft)
                            .withListener(layoutMenuButtonAnimatorListener)
                            .duration(300)
                            .playOn(layoutMenuButton);
                    closeMenuFragment();
                }
            }

            default: {
                break;
            }
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED: {

            }
            break;

            case DragEvent.ACTION_DRAG_ENTERED: {
                setRemoveAreaVisibility(View.VISIBLE);
                if (view.getId() == R.id.img_remove) {
                    removeArea.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bubble_animation));
                }
            }
            break;

            case DragEvent.ACTION_DRAG_LOCATION: {

            }
            break;

            case DragEvent.ACTION_DRAG_EXITED: {
                setRemoveAreaVisibility(View.INVISIBLE);
                if (view.getId() == R.id.img_remove) {
                    removeArea.setAnimation(null);
                }
            }
            break;

            case DragEvent.ACTION_DROP: {
                checkViewTakeActionDrop(view, dragEvent);
            }
            break;

            case DragEvent.ACTION_DRAG_ENDED: {

            }
            break;
        }
        return true;
    }

    private void checkViewTakeActionDrop(final View view, DragEvent dragEvent) {
        dragItem = (LaboratoryInstrument) dragEvent.getLocalState();
        switch (view.getId()) {
            case R.id.rl_main: {
                if (dragItem.getParent() == null) {
                    mainLayout.addView(dragItem);
                    if (dragItem instanceof LaboratoryHolderInstrument) {
                        LaboratoryHolderInstrument temp = (LaboratoryHolderInstrument) dragItem;
                        temp.setOnReactionListener(this);
                    }
                }
                calculateLocationForDragItem(dragEvent.getX(), dragEvent.getY(), dragItem);
                setRemoveAreaVisibility(View.INVISIBLE);
                removeArea.setAnimation(null);
            }
            break;

            case R.id.img_remove: {
                if (dragItem.getParent() == null) {
                    mainLayout.addView(dragItem);
                }
                calculateLocationForDragItem(view.getX(), view.getY(), dragItem);
                dragItem.startAnimation(itemRemovingAnimation);
                if (dragItem instanceof LaboratorySupportInstrument) {
                    List<LaboratoryInstrument> listItem = ((LaboratorySupportInstrument) dragItem).getListItemContain();
                    for (LaboratoryInstrument instrument : listItem) {
                        instrument.startAnimation(itemRemovingAnimation);
                    }
                }
                view.setAnimation(null);
                setRemoveAreaVisibility(View.INVISIBLE);
            }
            break;

            default: {
                break;
            }

        }
        dragItem.setVisibility(View.VISIBLE);
    }

    private void calculateLocationForDragItem(float xDrag, float yDrag, LaboratoryInstrument item) {
        int halfWidth = item.getWidthView() / 2;
        int halfHeight = item.getHeightView() / 2;
        float x = xDrag - halfWidth;
        float y = yDrag - halfHeight;

        if (xDrag - halfWidth < 0) {
            x = 0;
        }
        if (xDrag + halfWidth > mainLayoutWidth) {
            x = mainLayoutWidth - halfWidth * 2;
        }

        if (yDrag - halfHeight < 0) {
            y = 0;
        }

        if (yDrag + halfHeight > mainLayoutHeight) {
            y = mainLayoutHeight - halfHeight * 2;
        }

        item.setX(x);
        item.setY(y);
    }

    public void setRemoveAreaVisibility(int value) {
        removeArea.setVisibility(value);
    }

    public RelativeLayout getMainLayout() {
        return mainLayout;
    }

    public HeightChangingAnimationManager getHeightChangingAnimationManager() {
        return heightChangingAnimationManager;
    }

    public BubbleAnimationManager getBubbleAnimationManager() {
        return bubbleAnimationManager;
    }

    @Override
    public void reactionHappened(CharSequence equation) {
        closeMenuFragment();
        YoYo.with(Techniques.FadeOutUp).duration(500).delay(15000).withListener(equationAnimatorListener).playOn(equationsView);
        equationsView.setText(equation);
    }
}
