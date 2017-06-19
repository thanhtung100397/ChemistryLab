package com.chemistry.admin.chemistrylab.customview;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 8/18/2016.
 */
public class ChemicalSymbolView extends TextView {

    public ChemicalSymbolView(Context context) {
        super(context);
    }

    public ChemicalSymbolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChemicalSymbolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSymbol(CharSequence rawSymbolString) {//Only for index < 10
        setText(rawSymbolString, BufferType.SPANNABLE);
    }
}
