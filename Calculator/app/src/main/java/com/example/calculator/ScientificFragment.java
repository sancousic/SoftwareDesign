package com.example.calculator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ScientificFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scientific, container, false);
        ((Button)view.findViewById(R.id.sin)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.cos)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.tan)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.ln)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.lg)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.fact)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.pi)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.e)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id.pow)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.left)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.right)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.sqrt)).setOnClickListener(this::onOpClick);
        return view;
    }
}
