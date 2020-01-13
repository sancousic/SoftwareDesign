package com.example.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnBaseFragmentButtonClickListener {
    private TextView textView;
    private ScrollView mainScrollView;
    private EditText editText;
    private Button deleteButton;

    public static boolean isClear = true;
    public static boolean isInvalid = false;
    public static StringBuilder result = new StringBuilder("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.edit_text);
        mainScrollView = findViewById(R.id.scrollView);
        mainScrollView.post(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        init(savedInstanceState);

    }
    private void init(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            result = new StringBuilder(savedInstanceState.getString("result", ""));
            textView.setText(savedInstanceState.getString("textView", ""));
            editText.setText(savedInstanceState.getString("editText", ""));
            isClear = savedInstanceState.getBoolean("isClear", true);
            isInvalid = savedInstanceState.getBoolean(("isInvalid"), false);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("textView", textView.getText().toString());
        savedInstanceState.putString("editText", editText.getText().toString());
        savedInstanceState.putString("result", result.toString());
        savedInstanceState.putBoolean("isClear", isClear);
        savedInstanceState.putBoolean("isInvalid", isInvalid);
    }

    @Override
    public TextView getTextView() {
        return textView;
    }

    @Override
    public ScrollView getMainScrollView() {
        return mainScrollView;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }
}
