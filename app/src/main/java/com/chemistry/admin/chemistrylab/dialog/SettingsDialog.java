package com.chemistry.admin.chemistrylab.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.database.DatabaseManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation.BubbleAnimation;
import com.chemistry.admin.chemistrylab.effect_and_animation.bubble_animation.BubbleAnimationManager;
import com.chemistry.admin.chemistrylab.effect_and_animation.height_changing_animation.HeightChangingAnimationManager;

/**
 * Created by Admin on 10/19/2016.
 */

public class SettingsDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private TextView textSpeed;
    private SharedPreferences sharedPreferences;
    private double currentSpeed;

    public SettingsDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setOwnerActivity((Activity) context);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.settings_layout);
        initDialog(context);
    }

    private void initDialog(Context context) {
        SeekBar speedBar = (SeekBar) findViewById(R.id.seek_bar_speed);
        speedBar.setMax(19);
        textSpeed = (TextView) findViewById(R.id.txt_speed);
        sharedPreferences = context
                .getSharedPreferences(DatabaseManager.SETTINGS, Context.MODE_PRIVATE);
        currentSpeed = Double.parseDouble(sharedPreferences.getString(DatabaseManager.KEY_SPEED, "1.0"));
        speedBar.setOnSeekBarChangeListener(this);
        speedBar.setProgress((int) (currentSpeed * 10));
        textSpeed.setText(String.valueOf(currentSpeed));
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.ll_help).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textSpeed.setText(String.valueOf((progress + 1) * 1.0f / 10));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                double newSpeed = Double.parseDouble(textSpeed.getText().toString());
                BubbleAnimationManager.TIME_PER_LOOP -= (newSpeed - currentSpeed) * 40;
                HeightChangingAnimationManager.TIME_PER_LOOP -= (newSpeed - currentSpeed) * 900;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(DatabaseManager.KEY_SPEED, textSpeed.getText().toString());
                editor.apply();
                dismiss();
            }
            break;

            case R.id.btn_cancel: {
                dismiss();
            }

            case R.id.ll_about_us: {

            }
            break;

            case R.id.ll_help: {

            }
            break;
        }

    }
}
