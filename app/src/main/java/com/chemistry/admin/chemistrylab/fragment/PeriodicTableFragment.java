package com.chemistry.admin.chemistrylab.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.tooltip.ElementToolTip;
import com.chemistry.admin.chemistrylab.util.SymbolConverter;
import com.michael.easydialog.EasyDialog;

/**
 * Created by Admin on 10/14/2016.
 */
public class PeriodicTableFragment extends Fragment implements EasyDialog.OnEasyDialogDismissed, View.OnClickListener {
    private static final String TAG = "PTRagment";
    private ElementToolTip toolTip;
    private int fragmentWidth;
    private int fragmentHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.periodic_table_layout, container, false);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fragmentWidth = rootView.getWidth();
                fragmentHeight = rootView.getHeight();
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        Context context = getActivity();
        String arrElementName[] = DatabaseManager.getInstance(context).getAllElementSymbol();
        String packageName = context.getPackageName();
        Resources res = context.getResources();
        for (String anElementName : arrElementName) {
            rootView.findViewById(res.getIdentifier("btn_" + anElementName, "id", packageName))
                    .setOnClickListener(this);
        }
        toolTip = new ElementToolTip(context);
        return rootView;
    }

    @Override
    public void onDismissed() {
        View contentView = toolTip.getRootView();
        ((LinearLayout) contentView.getParent()).removeView(contentView);
    }

    @Override
    public void onClick(View v) {
        Context context = getActivity();
        Button button = (Button) v;
        ElementItem item = DatabaseManager.getInstance(context).getElement((button.getText().toString()));
        toolTip.setData(item);

        int easyDialogOrient;
        int widthDialog = toolTip.getRootView().getMeasuredWidth();
        int heightDialog = toolTip.getRootView().getMeasuredHeight();
        int xView = (int) v.getX();
        int yView = (int) v.getY();
        easyDialogOrient = EasyDialog.GRAVITY_TOP;
        if (yView - heightDialog < 0) {
            if(xView + widthDialog > fragmentWidth){
                easyDialogOrient = EasyDialog.GRAVITY_LEFT;
            }else if(xView - widthDialog < 0){
                easyDialogOrient = EasyDialog.GRAVITY_RIGHT;
            }else {
                easyDialogOrient = EasyDialog.GRAVITY_BOTTOM;
            }
        }

        new EasyDialog(context)
                .setLayout(toolTip.getRootView())
                .setLocationByAttachedView(v)
                .setGravity(easyDialogOrient)
                .setMatchParent(false)
                .setBackgroundColor(Color.WHITE)
                .setOnEasyDialogDismissed(this)
                .show();
    }

    public static class ElementItem {
        private String name;
        private double atomicMass;
        private int atomicNumber;
        private String symbol;
        private String electronConfig;
        private double electronicGravity;
        private String oxidationStates;

        public ElementItem(String name,
                           String symbol,
                           double atomicMass,
                           int atomicNumber,
                           String electronConfig,
                           double electronicGravity,
                           String oxidationStates) {
            this.name = name;
            this.symbol = symbol;
            this.atomicMass = atomicMass;
            this.atomicNumber = atomicNumber;
            this.electronConfig = electronConfig;
            this.electronicGravity = electronicGravity;
            this.oxidationStates = oxidationStates;
        }

        public String getName() {
            return name;
        }

        public double getAtomicMass() {
            return atomicMass;
        }

        public int getAtomicNumber() {
            return atomicNumber;
        }

        public String getSymbol() {
            return symbol;
        }

        public CharSequence getElectronConfig() {
            return SymbolConverter.getElectronConfig(electronConfig);
        }

        public double getElectronicGravity() {
            return electronicGravity;
        }

        public String getOxidationStates() {
            return oxidationStates;
        }


    }
}
