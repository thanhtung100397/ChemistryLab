package com.chemistry.admin.chemistrylab.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/20/2016.
 */
public class SymbolConverter {
    public static final float SUBSCRIPT_SPAN_SIZE = 0.7f;

    public static SpannableString convertToSymbol(String raw) {
        List<Integer> listIndexNumberPosition = new ArrayList<>();
        int length = raw.length();
        SpannableString convertSymbolString = new SpannableString(raw);

        for (int i = 0; i < length; i++) {
            if (Character.isDigit(raw.charAt(i))) {
                listIndexNumberPosition.add(i);
            }
        }

        for (Integer position : listIndexNumberPosition) {
            convertSymbolString.setSpan(new SubscriptSpan(), position, position + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            convertSymbolString.setSpan(new RelativeSizeSpan(SUBSCRIPT_SPAN_SIZE), position, position + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return convertSymbolString;
    }

    public static CharSequence getElectronConfig(String raw) {
        String parts[] = raw.split("_");
        CharSequence convertString = "";
        for (String aPart : parts) {
            SpannableString temp = new SpannableString(aPart);
            if(Character.isDigit(aPart.charAt(0))) {
                int length = aPart.length();
                temp.setSpan(new SuperscriptSpan(), 2, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                temp.setSpan(new RelativeSizeSpan(SUBSCRIPT_SPAN_SIZE), 2, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            convertString = TextUtils.concat(convertString, temp);
        }
        return convertString;
    }
}
