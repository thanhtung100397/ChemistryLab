package com.chemistry.admin.chemistrylab.effect_and_animation.spark_animation;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.activity.MainActivity;
import com.chemistry.admin.chemistrylab.customview.laboratory_instrument.holder_instrument.LaboratoryHolderInstrument;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by Admin on 10/18/2016.
 */
public class SparkAnimation {
    public static final int NUMBER_OF_PARTICLES = 50;
    public static final int PARTICLE_LIVE_TIME = 1500;
    public static final int PARTICLE_DRAWABLE_ID = R.drawable.ic_spark;
    private LaboratoryHolderInstrument holder;

    public SparkAnimation(LaboratoryHolderInstrument holder) {
        this.holder = holder;
    }

    public void start() {
        ParticleSystem particleSystem = new ParticleSystem((MainActivity) holder.getContext(),
                                                            NUMBER_OF_PARTICLES,
                                                            PARTICLE_DRAWABLE_ID,
                                                            PARTICLE_LIVE_TIME);
        particleSystem.setSpeedModuleAndAngleRange(0.0f, 0.4f, 225, 315)
                .setRotationSpeed(360)
                .setAcceleration(-0.0005f, 270)
                .setScaleRange(0.5f,1.3f)
                .emit(holder, 10, 5000);
    }


}
