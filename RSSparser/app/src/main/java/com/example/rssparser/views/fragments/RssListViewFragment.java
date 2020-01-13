package com.example.rssparser.views.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.rssparser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RssListViewFragment extends Fragment {
    private SearchView searchView;
    private TextView networkTextView;
    private ProgressBar progressBar;



    public RssListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.internet);
        searchView = view.findViewById(R.id.search);
        searchView.setQuery("https://www.wired.com/feed/category/gear/latest/rss",
                false);
        progressBar = view.findViewById(R.id.progressBar);

        Handler handler = new Handler((Message msg) -> {
            Bundle data = msg.getData();
            boolean isConnected = data.getBoolean("isConnected");
            if(isConnected)
                networkTextView.setText("Connected");
            else
                networkTextView.setText("Disconnected");
            return true;
        });

        Intent intent = new Intent(getActivity(),)
    }

}
