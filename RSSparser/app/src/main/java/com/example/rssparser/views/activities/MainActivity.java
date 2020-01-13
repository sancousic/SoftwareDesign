package com.example.rssparser.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.rssparser.R;
import com.example.rssparser.views.fragments.RssListViewFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag("RssListViewFragment") == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            RssListViewFragment fragment = new RssListViewFragment();
            transaction.replace(R.id.frameLayout, fragment, "RssListViewFragment");
            transaction.commit();
        }

        setContentView(R.layout.activity_main);
    }
}
