package com.chemistry.admin.chemistrylab.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.adapter.ListSubstancesPreviewAdapter;
import com.chemistry.admin.chemistrylab.customview.DragItemShadowBuilder;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.AlcoholBurner;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Breaker;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Flask;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.GasBottle;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Jar;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.TestTube;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.Trough;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.Fire;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.support_instrument.Tripod;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.tooltip.PreviewTip;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.michael.easydialog.EasyDialog;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/10/2016.
 */
public class LaboratoryFragment extends Fragment implements View.OnClickListener,
        Animator.AnimatorListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        EasyDialog.OnEasyDialogDismissed {
    public static final int EQUIPMENT_MODE = 0;
    public static final int LIQUID_MODE = 1;
    public static final int SOLID_MODE = 2;
    public static final int GAS_MODE = 3;
    private Drawable currentMenu;
    private int mode;
    private HorizontalListView listItemsView;
    private ListSubstancesPreviewAdapter adapter;
    private PreviewTip currentPreviewTip;
    private ProgressBar loadingBar;
    private LoadingTask loadingTask;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.laboratory_fragment, container, false);
        rootView.findViewById(R.id.btn_equipment).setOnClickListener(this);
        rootView.findViewById(R.id.btn_liquid).setOnClickListener(this);
        rootView.findViewById(R.id.btn_solid).setOnClickListener(this);
        rootView.findViewById(R.id.btn_gas).setOnClickListener(this);

        listItemsView = (HorizontalListView) rootView.findViewById(R.id.h_list_view);
        listItemsView.setOnItemLongClickListener(this);
        listItemsView.setOnItemClickListener(this);
        adapter = new ListSubstancesPreviewAdapter(getActivity());
        listItemsView.setAdapter(adapter);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mButton.getBackground().setLevel(0);
    }

    public void setmButton(Button mButton) {
        this.mButton = mButton;
    }

    private List<LaboratoryInstrument> loadEquipment() {
        List<LaboratoryInstrument> listEquipments = new ArrayList<>();
        Context context = getActivity();
        listEquipments.add(new Breaker(context,
                Breaker.BREAKER_STANDARD_WIDTH,
                Breaker.BREAKER_STANDARD_HEIGHT));
        listEquipments.add(new GasBottle(context,
                GasBottle.GAS_BOTTLE_STANDARD_WIDTH,
                GasBottle.GAS_BOTTLE_STANDARD_HEIGHT));
        listEquipments.add(new Jar(context,
                Jar.JAR_STANDARD_WIDTH,
                Jar.JAR_STANDARD_HEIGHT));
        listEquipments.add(new Flask(context,
                Flask.FLASK_STANDARD_WIDTH,
                Flask.FLASK_STANDARD_HEIGHT));
        listEquipments.add(new TestTube(context,
                TestTube.TEST_TUBE_STANDARD_WIDTH,
                TestTube.TEST_TUBE_STANDARD_HEIGHT));
        listEquipments.add(new Trough(context,
                Trough.TROUGH_STANDARD_WIDTH,
                Trough.TROUGH_STANDARD_HEIGHT));
        listEquipments.add(new Tripod(context));
        listEquipments.add(new AlcoholBurner(context));
        return listEquipments;
    }

    private void openHorizontalListViewFragment() {
        loadingTask = new LoadingTask();
        loadingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void closeHorizontalListViewFragment() {
        if (listItemsView.getVisibility() == View.VISIBLE) {
            listItemsView.setVisibility(View.GONE);
        }
        if (loadingBar.getVisibility() == View.VISIBLE) {
            loadingBar.setVisibility(View.GONE);
            loadingTask.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        Drawable background = view.getBackground();
        switch (view.getId()) {
            case R.id.btn_equipment: {
                if (background.getLevel() == 0) {
                    mode = EQUIPMENT_MODE;
                    openHorizontalListViewFragment();
                    background.setLevel(1);
                    if (currentMenu != null) {
                        currentMenu.setLevel(0);
                    }
                    currentMenu = background;
                } else {
                    background.setLevel(0);
                    currentMenu = null;
                    YoYo.with(Techniques.SlideOutUp).withListener(this).duration(400).playOn(listItemsView);
                }
            }
            break;

            case R.id.btn_liquid: {
                if (background.getLevel() == 0) {
                    mode = LIQUID_MODE;
                    openHorizontalListViewFragment();
                    background.setLevel(1);
                    if (currentMenu != null) {
                        currentMenu.setLevel(0);
                    }
                    currentMenu = background;
                } else {
                    background.setLevel(0);
                    currentMenu = null;
                    YoYo.with(Techniques.SlideOutUp).withListener(this).duration(400).playOn(listItemsView);
                }
            }
            break;

            case R.id.btn_solid: {
                if (background.getLevel() == 0) {
                    mode = SOLID_MODE;
                    openHorizontalListViewFragment();
                    background.setLevel(1);
                    if (currentMenu != null) {
                        currentMenu.setLevel(0);
                    }
                    currentMenu = background;
                } else {
                    background.setLevel(0);
                    currentMenu = null;
                    YoYo.with(Techniques.SlideOutUp).withListener(this).duration(400).playOn(listItemsView);
                }
            }
            break;

            case R.id.btn_gas: {
                if (background.getLevel() == 0) {
                    mode = GAS_MODE;
                    openHorizontalListViewFragment();
                    background.setLevel(1);
                    if (currentMenu != null) {
                        currentMenu.setLevel(0);
                    }
                    currentMenu = background;
                } else {
                    background.setLevel(0);
                    currentMenu = null;
                    YoYo.with(Techniques.SlideOutUp).withListener(this).duration(400).playOn(listItemsView);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        closeHorizontalListViewFragment();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
        LaboratoryInstrument dragItem = ((ListSubstancesPreviewAdapter) adapterView.getAdapter()).getItem(i).getClone();
        DragItemShadowBuilder shadowBuilder = new DragItemShadowBuilder(dragItem);
        view.startDrag(null, shadowBuilder, dragItem, View.DRAWING_CACHE_QUALITY_AUTO);
        ((MainActivity) getActivity()).setRemoveAreaVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode != EQUIPMENT_MODE) {
            currentPreviewTip = ((ListSubstancesPreviewAdapter.ViewHolder) view.getTag()).getPreviewTip();
            EasyDialog toolTip = new EasyDialog(getActivity())
                    .setLayout(currentPreviewTip)
                    .setLocationByAttachedView(view)
                    .setGravity(EasyDialog.GRAVITY_BOTTOM)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setBackgroundColor(Color.WHITE);
            toolTip.setOnEasyDialogDismissed(this);
            toolTip.show();
        }
    }

    @Override
    public void onDismissed() {
        ((LinearLayout) currentPreviewTip.getParent()).removeView(currentPreviewTip);
    }

    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (mode) {
                case EQUIPMENT_MODE: {
                    adapter.setListItems(loadEquipment(), mode);
                }
                break;

                case SOLID_MODE: {
                    adapter.setListItems(DatabaseManager.getInstance(getActivity()).getAllSolidPreview(), mode);
                }
                break;

                case LIQUID_MODE: {
                    adapter.setListItems(DatabaseManager.getInstance(getActivity()).getAllLiquidPreview(), mode);
                }
                break;

                case GAS_MODE: {
                    adapter.setListItems(DatabaseManager.getInstance(getActivity()).getAllGasPreview(), mode);
                }
                break;

                default: {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            loadingBar.setVisibility(View.GONE);
            listItemsView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInLeft).duration(400).playOn(listItemsView);
        }
    }
}
