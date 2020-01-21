package com.example.calculator;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

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

    @Override
    public void onOpClick(View view) {
        if(MainActivity.isInvalid) clearEdit();
        Button btn = (Button)view;
        String op_eval;
        String op = btn.getText().toString();
        if(op.equals("lg")) {
            op_eval = "log10(";
            op = "lg(";
        }
        else if(op.length() > 1 && !op.equalsIgnoreCase("pi"))
            op_eval = op = op + "(";
        else
            op_eval = op;
        if(!MainActivity.isClear && MainActivity.result.length() > 0 && op_eval.length() < 2 && !op.equals("(")
                && MainActivity.result.charAt(MainActivity.result.length() - 1) != '(')
        {
            // if prev is num or ) or ! add op in end
            if(Character.isDigit(MainActivity.result.charAt(MainActivity.result.length() -1))
                    || MainActivity.result.charAt(MainActivity.result.length()-1) == ')'
                    || isPorE(MainActivity.result)
                    || MainActivity.result.charAt(MainActivity.result.length() - 1) == '!') {
                handler.getEditText().append(op);
                MainActivity.result.append(op_eval);
            }
            // else replace operator to newer
            else {
                if (MainActivity.result.length() > 1) {
                    Editable txt_sign = handler.getEditText().getText();
                    handler.getEditText().setText(txt_sign.replace(txt_sign.length()-1,txt_sign.length(), op));
                    MainActivity.result.replace(MainActivity.result.length()-1,MainActivity.result.length(), op_eval);
                }
            }
        }
        // if operator == ( or -
        else if(op_eval.length() > 1 || op.equals("(") || op_eval.equals("-")) {
            MainActivity.isClear = false;
            // and if prev is digit or ) or Pi or e append * and op
            if (MainActivity.result.length() > 0
                    && (Character.isDigit(MainActivity.result.charAt(MainActivity.result.length() - 1))
                    || MainActivity.result.charAt(MainActivity.result.length() - 1) == ')'
                    || MainActivity.result.charAt(MainActivity.result.length() -1) == '!'
                    || isPorE(MainActivity.result)))
            {
                MainActivity.result.append("*");
                handler.getEditText().append("*");
            }
            handler.getEditText().append(op);
            MainActivity.result.append(op_eval);
        }
        handler.getMainScrollView().fullScroll(ScrollView.FOCUS_DOWN);
    }
    @Override
    public void onNumClick(View view)
    {
        if(MainActivity.isInvalid) clearEdit();
        Button button = (Button)view;
        String num = button.getText().toString();
        if (MainActivity.isClear){
            handler.getTextView().append("\n");
            handler.getEditText().setText(num);
            MainActivity.isClear = false;
            MainActivity.result.setLength(0);
        }
        else if((Character.isDigit(MainActivity.result.charAt(MainActivity.result.length() -1))
                || isPorE(MainActivity.result)))
        {
            MainActivity.result.append("*");
            handler.getEditText().append("*");
            handler.getEditText().append(num);
        }
        else{
            handler.getEditText().append(num);
        }
        MainActivity.result.append(num);
    }
}
