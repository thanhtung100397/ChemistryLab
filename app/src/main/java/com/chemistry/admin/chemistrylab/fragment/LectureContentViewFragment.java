package com.chemistry.admin.chemistrylab.fragment;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chemistry.admin.chemistrylab.R;
import com.joanzapata.pdfview.PDFView;

/**
 * Created by Admin on 10/19/2016.
 */

public class LectureContentViewFragment extends Fragment {
    private PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lecture_content_view_fragment, container, false);
        Bundle receiverBundle = getArguments();
        pdfView = (PDFView) rootView.findViewById(R.id.pdf_view);
        pdfView.fromAsset(receiverBundle.getString(LectureFragment.KEY_PDF_PATH))
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                .swipeVertical(true)
                .load();
        pdfView.zoomTo(3.0f);
        return rootView;
    }
}
