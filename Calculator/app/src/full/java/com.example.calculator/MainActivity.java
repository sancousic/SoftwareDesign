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

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ScrollView mainScrollView;
    EditText editText;
    Button deleteButton;

    /*SharedPreferences preferences;
    static final String APP_PREFERECNES = "settings";*/
    boolean isClear = true;
    boolean isInvalid = false;
    //boolean isClearNum = true;
    StringBuilder result = new StringBuilder("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteButton = findViewById(R.id.delete);
        remove();
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                editText.setText("");
                result.setLength(0);
                textView.setText("");
                isClear = true;
                return true;
            }
        });
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.edit_text);
        mainScrollView = findViewById(R.id.scrollView);
        mainScrollView.post(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        /*preferences = getSharedPreferences(APP_PREFERECNES, Context.MODE_PRIVATE);
        result = new StringBuilder(preferences.getString("result", "0"));
        textView.setText(preferences.getString("textView", ""));
        editText.setText(preferences.getString("editText", ""));
        isClear = preferences.getBoolean("isClear", true);
        isClearNum = preferences.getBoolean("isClearNum", true);*/
        if(savedInstanceState != null) {
            result = new StringBuilder(savedInstanceState.getString("result", ""));
            textView.setText(savedInstanceState.getString("textView", ""));
            editText.setText(savedInstanceState.getString("editText", ""));
            isClear = savedInstanceState.getBoolean("isClear", true);
            isInvalid = savedInstanceState.getBoolean(("isInvalid"), false);
            //isClearNum = savedInstanceState.getBoolean("isClearNum", true);
        }
    }
    protected void remove()
    {
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            GridLayout layout = findViewById(R.id.baseFragment);
            Button button = findViewById(R.id.scl);
            layout.removeView(button);
            Button eqB = findViewById(R.id.equals);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(eqB.getLayoutParams());
            params.columnSpec = GridLayout.spec(0, 4, 1);
            eqB.setLayoutParams(params);
        }
    }
    /*@Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("textView", textView.getText().toString());
        ed.putString("editText", editText.getText().toString());
        ed.putString("result", result.toString());
        ed.putBoolean("isClear", isClear);
        ed.putBoolean("isClearNum", isClearNum);
        ed.apply();
    }*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("textView", textView.getText().toString());
        savedInstanceState.putString("editText", editText.getText().toString());
        savedInstanceState.putString("result", result.toString());
        savedInstanceState.putBoolean("isClear", isClear);
        savedInstanceState.putBoolean("isInvalid", isInvalid);
        //savedInstanceState.putBoolean("isClearNum", isClearNum);
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
    protected void clearEdit(){
        isInvalid = false;
        result.setLength(0);
        editText.setText("");
    }
    public void onOpClick(View view)
    {
        //isClearNum = false;
        if(isInvalid) clearEdit();
        Button btn = (Button)view;
        String op = btn.getText().toString();
        switch (op)
        {
            case "del":
                Editable txt = editText.getText();
                if(editText.length() > 0) {
                    do {
                        editText.setText(txt.delete(txt.length() - 1, txt.length()));
                    }
                    while (txt.length() > 0 && Character.isLetter(txt.charAt(txt.length() - 1))
                            && !isPorE(txt));
                }
                else {
                    isClear = true;
                }
                if(result.length() > 0) {
                    if(result.length() >= 6
                            && result.substring(result.length() - 6, result.length()).equals("log10("))
                    {
                        result.setLength(result.length() - 6);
                        return;
                    };
                    do {
                        result.setLength(result.length() - 1);
                    }
                    while (result.length() > 0 && (Character.isLetter(result.charAt(result.length() -1))) && !isPorE(result));
                }
                else isClear = true;
                return;
            case "=":
                /*if(result.indexOf("(") != -1 && result.indexOf(")") == -1)
                {

                    result.append(")");
                }*/
                if(result.length() > 0) {
                    long count = result.chars().filter(ch -> ch == '(').count();
                    for (long i = 0; i < count; i++) {
                        result.append(')');
                    }
                    String res = Calculator.exec(result.toString());
                    if (!Character.isDigit(res.charAt(0))
                            && res.charAt(0) != '-') {
                        isInvalid = true;
                    }
                    result = new StringBuilder(res);
                    textView.append(editText.getText());
                    editText.setText(result);
                    textView.append("\n");
                    break;
                }
            default:
                String op_eval;
                if(op.equals("lg")) {
                    op_eval = "log10(";
                    op = "lg(";
                }
                else if(op.length() > 1) op_eval = op = op + "(";
                else op_eval = op;
                // if result not empty and prev is't (
                if(!isClear && result.length() > 0 && op_eval.length() < 2 && !op.equals("(")
                    && result.charAt(result.length() - 1) != '(')
                {
                    // if prev is num or ) or ! add op in end
                    if(Character.isDigit(result.charAt(result.length() -1))
                        || result.charAt(result.length()-1) == ')'
                        || isPorE(result)
                        || result.charAt(result.length() - 1) == '!') {
                        editText.append(op);
                        result.append(op_eval);
                    }
                    // else replace operator to newer
                    else {
                        if (result.length() > 1) {
                            Editable txt_sign = editText.getText();
                            editText.setText(txt_sign.replace(txt_sign.length()-1,txt_sign.length(), op));
                            result.replace(result.length()-1,result.length(), op_eval);
                        }
                    }
                }
                // if operator == ( or -
                else if(op_eval.length() > 1 || op.equals("(") || op_eval.equals("-")) {
                    isClear = false;
                    // and if prev is digit or ) or Pi or e append * and op
                    if (result.length() > 0
                            && (Character.isDigit(result.charAt(result.length() - 1))
                            || result.charAt(result.length() - 1) == ')'
                            || result.charAt(result.length() -1) == '!'
                            || isPorE(result)))
                    {
                        result.append("*");
                        editText.append("*");
                    }
                    editText.append(op);
                    result.append(op_eval);
                }
                break;
        }
        mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }


    public void onNumberClick(View view){
        if(isInvalid) clearEdit();
        Button button = (Button)view;
        String num = button.getText().toString();
        if (isClear){
            textView.append("\n");
            editText.setText(num);
            isClear = false;
            //isClearNum = false;
            result.setLength(0);
        }
        else if(result.length() > 0 && (isPorE(result) || result.charAt(result.length() - 1) == ')'
                || result.charAt(result.length() - 1) == '!'
                || (num.equals("e") && Character.isDigit(result.charAt(result.length() - 1)))))
        {
            result.append("*");
            editText.append("*");
            editText.append(num);
        }
        else{
            editText.append(num);
        }
        result.append(num);
    }
    public void onSciClick(View view) {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        if(findViewById(R.id.frm).getHeight() == 0){
            Fragment sciFragment = new ScientificFragment();
            transaction.add(R.id.frm, sciFragment);
        }
        else {
            transaction.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frm)));
        }
        transaction.commit();
    }
}
