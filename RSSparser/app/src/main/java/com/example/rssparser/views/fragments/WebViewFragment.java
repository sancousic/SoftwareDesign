package com.example.rssparser.views.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.rssparser.R;
import com.example.rssparser.viewModels.WebViewViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {
    private WebView webView;
    private ProgressBar progressBar;

    public WebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.webViewProgressBar);
        webView = view.findViewById(R.id.webView);

        WebViewViewModel feedViewModel = new ViewModelProvider(getActivity()).get(WebViewViewModel.class);
        String link = feedViewModel.getLink();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.loadUrl(link);
    }

}
