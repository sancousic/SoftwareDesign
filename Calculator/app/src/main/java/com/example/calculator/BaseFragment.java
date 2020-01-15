package com.example.calculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class BaseFragment extends Fragment {
    private OnBaseFragmentButtonClickListener handler;
    public BaseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ((Button)view.findViewById(R.id._0)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._1)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._2)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._3)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._4)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._5)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._6)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._7)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._8)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id._9)).setOnClickListener(this::onNumClick);
        ((Button)view.findViewById(R.id.delete)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.delete)).setOnLongClickListener(this::onLongDeleteClick);
        ((Button)view.findViewById(R.id.mul)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.div)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.add)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.sub)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.dot)).setOnClickListener(this::onOpClick);
        ((Button)view.findViewById(R.id.equals)).setOnClickListener(this::onOpClick);
        Button sciB = (Button)view.findViewById(R.id.scl);
        if(sciB != null) sciB.setOnClickListener(this::onSciClick);
        return view;
    }
    private boolean onLongDeleteClick(View view) {
        handler.getEditText().setText("");
        MainActivity.result.setLength(0);
        handler.getTextView().setText("");
        MainActivity.isClear = true;
        return true;
    }
    @Override
    public void onDetach() { super.onDetach(); }

    public void onSciClick(View view) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(getActivity().findViewById(R.id.frm).getHeight() == 0){
            Fragment sciFragment = new ScientificFragment();
            transaction.add(R.id.frm, sciFragment);
        }
        else {
            transaction.remove(Objects.requireNonNull(getActivity().getSupportFragmentManager().findFragmentById(R.id.frm)));
        }
        transaction.commit();
    }
    boolean isPorE(StringBuilder str){
        return (str.length() > 0 && str.charAt(str.length() - 1) == 'e')
                || (str.length() > 1 && str.charAt(str.length() - 2) == 'p'
                && str.charAt(str.length() - 1) == 'i');
    }
    boolean isPorE(Editable str){
        return (str.length() > 0 && (str.charAt(str.length() - 1) == 'e')
                || (str.length() > 1 && str.charAt(str.length() - 2) == 'p'
                && str.charAt(str.length() - 1) == 'i'));
    }
    boolean isPorE(String str){
        return (str.length() > 0 && (str.charAt(str.length() - 1) == 'e')
                || (str.length() > 1 && str.charAt(str.length() - 2) == 'p'
                && str.charAt(str.length() - 1) == 'i'));
    }
    protected void clearEdit(){
        MainActivity.isInvalid = false;
        MainActivity.result.setLength(0);
        handler.getEditText().setText("");
    }

    public void onOpClick(View view)
    {
        if(MainActivity.isInvalid) clearEdit();
        Button btn = (Button)view;
        String op = btn.getText().toString();
        switch (op)
        {
            case "del":
                Editable txt = handler.getEditText().getText();
                if(handler.getEditText().length() > 0) {
                    do {
                        handler.getEditText().setText(txt.delete(txt.length() - 1, txt.length()));
                    }
                    while (txt.length() > 0 && Character.isLetter(txt.charAt(txt.length() - 1))
                            && !isPorE(txt));
                }
                else {
                    MainActivity.isClear = true;
                }
                if(MainActivity.result.length() > 0) {
                    if(MainActivity.result.length() >= 6
                            && MainActivity.result.substring(MainActivity.result.length() - 6, MainActivity.result.length()).equals("log10("))
                    {
                        MainActivity.result.setLength(MainActivity.result.length() - 6);
                        return;
                    };
                    do {
                        MainActivity.result.setLength(MainActivity.result.length() - 1);
                    }
                    while (MainActivity.result.length() > 0 && (Character.isLetter(MainActivity.result.charAt(MainActivity.result.length() -1))) && !isPorE(MainActivity.result));
                }
                else MainActivity.isClear = true;
                return;
            case "=":
                if(MainActivity.result.length() > 0) {
                    if("/*-+.".indexOf(MainActivity.result.charAt(MainActivity.result.length() - 1)) >= 0)
                    {
                        MainActivity.result.setLength(MainActivity.result.length() -1);
                    }
                    long count = MainActivity.result.chars().filter(ch -> ch == '(').count();
                    for (long i = 0; i < count; i++) {
                        MainActivity.result.append(')');
                    }
                    String res = Calculator.exec(MainActivity.result.toString());
                    if (MainActivity.result.length() > 1 && (Character.isDigit(res.charAt(1))
                            || Character.isDigit(res.charAt(0)))) {
                        MainActivity.isInvalid = false;
                    }
                    else MainActivity.isInvalid = true;
                    MainActivity.result = new StringBuilder(res);
                    handler.getTextView().append(handler.getEditText().getText());
                    handler.getEditText().setText(MainActivity.result);
                    handler.getTextView().append("\n");
                    break;
                }
            default:
                String op_eval;
                if(op.equals("lg")) {
                    op_eval = "log10(";
                    op = "lg(";
                }
                else if(op.length() > 1 && !op.equalsIgnoreCase("pi"))
                    op_eval = op = op + "(";
                else
                    op_eval = op;
                // if result not empty and prev is't (
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
                break;
        }
        handler.getMainScrollView().fullScroll(ScrollView.FOCUS_DOWN);
    }
    public void onNumClick(View view){
        if(MainActivity.isInvalid) clearEdit();
        Button button = (Button)view;
        String num = button.getText().toString();
        if (MainActivity.isClear){
            handler.getTextView().append("\n");
            handler.getEditText().setText(num);
            MainActivity.isClear = false;
            MainActivity.result.setLength(0);
        }
        //TODO rewrite this shit
        else if(MainActivity.result.length() > 0
                && (!isPorE(num) && (isPorE(MainActivity.result)
                || MainActivity.result.charAt(MainActivity.result.length() - 1) == '!')
                || (isPorE(num)
                && (Character.isDigit(MainActivity.result.charAt(MainActivity.result.length() -1))
                || isPorE(MainActivity.result)))))
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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity activity;
        if(context instanceof Activity)
        {
            activity = (Activity) context;
            try {
                this.handler = (OnBaseFragmentButtonClickListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " should implement OnBaseFragmentButtonClickListener");
            }
        }

    }

    public interface OnBaseFragmentButtonClickListener {
        TextView getTextView();
        ScrollView getMainScrollView();
        EditText getEditText();
    }
}
