package com.chemistry.admin.chemistrylab.fragment;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.activity.DocumentActivity;

/**
 * Created by Admin on 10/16/2016.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CategoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);
        rootView.findViewById(R.id.img_periodic_table).setOnClickListener(this);
        rootView.findViewById(R.id.img_lecture).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_periodic_table:{
                DocumentActivity documentActivity = (DocumentActivity) getActivity();
                documentActivity.showFragment(new PeriodicTableFragment());
            }
            break;

            case R.id.img_lecture:{
                DocumentActivity documentActivity = (DocumentActivity) getActivity();
                documentActivity.showFragment(new LectureFragment());
            }
            break;

            default:{
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
