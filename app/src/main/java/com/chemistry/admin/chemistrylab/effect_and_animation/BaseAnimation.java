package com.chemistry.admin.chemistrylab.effect_and_animation;

import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;

/**
 * Created by Admin on 10/9/2016.
 */
public interface BaseAnimation {
    boolean run();
    void updateUI();
    LaboratoryHolderInstrument getHolder();
    void onStop();
    boolean isPaused();
}
