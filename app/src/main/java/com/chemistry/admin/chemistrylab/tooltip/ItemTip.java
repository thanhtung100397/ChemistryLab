package com.chemistry.admin.chemistrylab.tooltip;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.chemical.liquid.Liquid;
import com.chemistry.admin.chemistrylab.chemical.solid.Solid;

/**
 * Created by Admin on 10/5/2016.
 */
public class ItemTip extends RelativeLayout {
    private static final String TAG = "ItemToolTip";
    private TextView textMole;
    private Substance substance;
    private double previousMoleValue;

    public ItemTip(Context context, Substance substance) {
        super(context);
        this.substance = substance;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_information, this, false);
        TextView textSymbol = ((TextView) view.findViewById(R.id.txt_symbol));
        textSymbol.setText(substance.getConvertSymbol());
        if(substance instanceof Solid){
            textSymbol.setTextColor(context.getResources().getColor(R.color.solid_tip_color));
        }else if(substance instanceof Liquid){
            textSymbol.setTextColor(context.getResources().getColor(R.color.liquid_tip_color));
        }else {
            textSymbol.setTextColor(context.getResources().getColor(R.color.gas_tip_color));
        }
        textMole = (TextView) view.findViewById(R.id.txt_mole);
        previousMoleValue = Math.round(substance.getMole() * 100) * 1.0 / 100;
        if(previousMoleValue != 0) {
            update();
        }else {
            textMole.setText("0");
        }
        addView(view);
    }

    public ItemTip getClone(Substance substance) {
        return new ItemTip(getContext(), substance);
    }

    public void update() {
        if(previousMoleValue == 0 && substance.getMole() == 0){
            substance.removeFromContainer();
            return;
        }
        double moleRounded = Math.round(substance.getMole() * 100) * 1.0 / 100;
        if(moleRounded > previousMoleValue){
            textMole.setTextColor(Color.GREEN);
        }else if(moleRounded < previousMoleValue){
            textMole.setTextColor(Color.RED);
        }
        previousMoleValue = moleRounded;
        textMole.setText(String.valueOf(moleRounded));
    }

    public void destroy(){
        LinearLayout containerView = (LinearLayout) getParent();
        if(containerView != null) {
            containerView.removeView(this);
        }
    }
}
