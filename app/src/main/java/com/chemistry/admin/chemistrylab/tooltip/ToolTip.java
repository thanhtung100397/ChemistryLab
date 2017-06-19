package com.chemistry.admin.chemistrylab.tooltip;

import android.view.View;
import android.widget.LinearLayout;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.michael.easydialog.EasyDialog;

/**
 * Created by Admin on 10/12/2016.
 */
public class ToolTip extends LinearLayout implements View.OnClickListener {
    public static final int TOOL_TIP_WIDTH = 270;
    private LaboratoryHolderInstrument holder;
    private LinearLayout listItemLayout;
    private EasyDialog toolTip;

    public ToolTip(LaboratoryHolderInstrument holder) {
        super(holder.getContext());
        this.holder = holder;
        initViews();
        setLayoutParams(new LinearLayout.LayoutParams(TOOL_TIP_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void setToolTip(EasyDialog toolTip) {
        this.toolTip = toolTip;
    }

    private void initViews() {
        View rootView = View.inflate(getContext(), R.layout.holder_tip, this);
        rootView.findViewById(R.id.btn_clear).setOnClickListener(this);
        rootView.findViewById(R.id.btn_remove).setOnClickListener(this);
        listItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_list_item);
    }

    public void addItem(ItemTip item) {
        listItemLayout.addView(item);
    }

    public void removeAllItem() {
        listItemLayout.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear: {
                holder.removeAllSubstance();
            }
            break;

            case R.id.btn_remove: {
                ((MainActivity) getContext()).getMainLayout().removeView(holder);
                toolTip.dismiss();
            }
            break;

            default: {
                break;
            }
        }
    }
}
