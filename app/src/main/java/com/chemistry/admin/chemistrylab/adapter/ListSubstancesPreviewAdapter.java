package com.chemistry.admin.chemistrylab.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.customview.ChemicalSymbolView;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.LaboratoryInstrument;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.chemistry.admin.chemistrylab.fragment.LaboratoryFragment;
import com.chemistry.admin.chemistrylab.tooltip.PreviewTip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/19/2016.
 */
public class ListSubstancesPreviewAdapter extends BaseAdapter {
    private static final String TAG = "HListViewAdapter";

    private Context context;
    private LayoutInflater inflater;
    private List<? extends LaboratoryInstrument> listItems;
    private int mode;

    public ListSubstancesPreviewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        listItems = new ArrayList<>();
    }

    public void setListItems(List<? extends LaboratoryInstrument> listItems, int mode) {
        this.listItems = listItems;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public LaboratoryInstrument getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_instrument, viewGroup, false);
            viewHolder.reviewInstrument = (ImageView) view.findViewById(R.id.img_review_instrument);
            viewHolder.textSymbol = (ChemicalSymbolView) view.findViewById(R.id.txt_name);
            if (mode == LaboratoryFragment.EQUIPMENT_MODE) {
                viewHolder.textSymbol.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.equipment_font));
            } else {
                viewHolder.textSymbol.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.substance_font));
            }
            viewHolder.previewTip = new PreviewTip(context, this);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reviewInstrument.setImageBitmap(listItems.get(i).createReviewImage(120));
        viewHolder.textSymbol.setSymbol(getItem(i).getName());
        if (mode != LaboratoryFragment.EQUIPMENT_MODE) {
            viewHolder.previewTip.setSubstance(((LaboratoryHolderInstrument) listItems.get(i)).getDefaultSubstance());
        }
        return view;
    }

    public static class ViewHolder {
        private ImageView reviewInstrument;
        private ChemicalSymbolView textSymbol;
        private PreviewTip previewTip;

        public PreviewTip getPreviewTip() {
            return previewTip;
        }
    }
}
