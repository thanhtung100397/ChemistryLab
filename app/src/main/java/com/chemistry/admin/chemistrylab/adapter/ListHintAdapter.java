package com.chemistry.admin.chemistrylab.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.chemical.Substance;
import com.chemistry.admin.chemistrylab.customview.ChemicalSymbolView;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/13/2016.
 */

public class ListHintAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<Substance> listHints;

    public ListHintAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        listHints = new ArrayList<>();
    }

    public void search(String text){
        listHints.clear();
        if(text.isEmpty()){
            return;
        }
        listHints.addAll(DatabaseManager.getInstance(context).findSubstancesByName(text));
        notifyDataSetChanged();
    }

    public void clearListItems(){
        listHints.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listHints.size();
    }

    @Override
    public Substance getItem(int position) {
        return listHints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup container) {
        ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.item_hint, container, false);
            viewHolder = new ViewHolder();
            viewHolder.textSymbol = (ChemicalSymbolView) view.findViewById(R.id.txt_hint_content);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Substance substance = listHints.get(i);
        CharSequence content = TextUtils.concat(substance.getName() +" - ", substance.getConvertSymbol());
        viewHolder.textSymbol.setSymbol(content);
        return view;
    }

    private class ViewHolder{
        ChemicalSymbolView textSymbol;
    }
}
