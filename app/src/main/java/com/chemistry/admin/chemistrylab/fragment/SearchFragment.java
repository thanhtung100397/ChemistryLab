package com.chemistry.admin.chemistrylab.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.chemistry.admin.chemistrylab.R;
import com.chemistry.admin.chemistrylab.adapter.ListHintAdapter;

import java.util.Locale;

/**
 * Created by Admin on 10/13/2016.
 */

public class SearchFragment extends Fragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private EditText searchBar;
    private ListView listHint;
    private ListHintAdapter adapter;
    private Button mButton;
    private WebView webView;
    private ProgressBar loadingBar;
    private ImageButton buttonSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        searchBar = (EditText) rootView.findViewById(R.id.edt_search_bar);
        searchBar.addTextChangedListener(this);
        listHint = (ListView) rootView.findViewById(R.id.list_hint);
        listHint.setOnItemClickListener(this);
        adapter = new ListHintAdapter(getActivity());
        listHint.setAdapter(adapter);
        buttonSearch = (ImageButton) rootView.findViewById(R.id.btn_search);
        buttonSearch.setOnClickListener(this);

        webView = (WebView) rootView.findViewById(R.id.web_view);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(webView.getVisibility() == View.INVISIBLE) {
                    webView.setVisibility(View.VISIBLE);
                }
                loadingBar.setVisibility(View.INVISIBLE);
                buttonSearch.setVisibility(View.VISIBLE);
            }
        };
        webView.setWebViewClient(webViewClient);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mButton.getBackground().setLevel(0);
        searchBar.setText("");
        adapter.clearListItems();
        listHint.setVisibility(View.INVISIBLE);
    }

    public void setmButton(Button mButton) {
        this.mButton = mButton;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search: {
                adapter.clearListItems();
                listHint.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                String content = searchBar.getText().toString().toLowerCase(Locale.getDefault());
                webView.loadUrl("https://vi.wikipedia.org/wiki/" + content);
            }
            break;

            default: {
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListHintAdapter adapter = (ListHintAdapter) parent.getAdapter();
        String content = adapter.getItem(position).getName();
        searchBar.setText(content);
        adapter.clearListItems();
        listHint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String lowerText = s.toString().toLowerCase(Locale.getDefault());
        if (lowerText.isEmpty()) {
            listHint.setVisibility(View.INVISIBLE);
        } else {
            listHint.setVisibility(View.VISIBLE);
        }
        adapter.search(s.toString());
    }
}
