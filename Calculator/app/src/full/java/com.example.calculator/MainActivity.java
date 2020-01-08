package com.example.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    SharedPreferences preferences;
    static final String APP_PREFERECNES = "settings";
    boolean isClear = true;
    boolean isClearNum = true;
    StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteButton = findViewById(R.id.delete);
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                editText.setText("");
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
        preferences = getSharedPreferences(APP_PREFERECNES, Context.MODE_PRIVATE);
        result = new StringBuilder(preferences.getString("result", "0"));
        textView.setText(preferences.getString("textView", ""));
        editText.setText(preferences.getString("editText", ""));
        isClear = preferences.getBoolean("isClear", true);
        isClearNum = preferences.getBoolean("isClearNum", true);
    }
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor ed =preferences.edit();
        ed.putString("textView", textView.getText().toString());
        ed.putString("editText", editText.getText().toString());
        ed.putString("result", result.toString());
        ed.putBoolean("isClear", isClear);
        ed.putBoolean("isClearNum", isClearNum);
        ed.apply();
    }
    boolean isPorE(StringBuilder str){
        return (str.length() > 0 && str.charAt(str.length() - 1) == 'e')
                || (str.length() > 1 && str.charAt(str.length() - 3) == 'p' && str.charAt(str.length() - 2) == 'i');
    }
    boolean isPorE(Editable str){
        return (str.length() > 0 && (str.charAt(str.length() - 1) == 'e'
                || str.charAt(str.length() - 2) == 'p' && str.charAt(str.length() - 1) == 'i'));
    }
    public void onOpClick(View view)
    {
        isClearNum = false;

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
                    return;
                }
                else {
                    isClear = true;
                }
                return;
            case "=":
                double res = Calculator.exec(result.toString());
                if(Double.isNaN(res))
                {
                    editText.setText("");
                    result.setLength(0);
                    textView.append(getString(R.string.invalid));
                }
                else
                {
                    if(res % 1 == 0)
                    {
                        result = new StringBuilder(Integer.toString((int)res));
                    }
                    else
                    {
                        result = new StringBuilder(Double.toString(res));
                    }
                    textView.append(editText.getText());
                    StringBuilder res_text = new StringBuilder(result);
                    editText.setText(res_text.insert(0, "= "));
                }
                textView.append("\n");
                isClearNum = true;
                break;
            default:;
                if(!isClear && result.length() > 0)
                {
                    if(Character.isDigit(result.charAt(result.length() - 1))) {
                        editText.append(op);
                        result.append(op);
                    }
                    else {
                        if (result.length() > 1)
                        {
                            Editable txt_sighn =editText.getText();
                            editText.setText(txt_sighn.replace(txt_sighn.length() - 1, txt_sighn.length(), op));
                            result.replace(result.length() - 1, result.length(), op);
                        }
                    }

                }
                else if (op.equals("-")) {
                    isClear = false;
                    editText.append(op);
                    result.append(op);
                }
                break;
        }
        mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
    public void onNumberClick(View view){
        Button button = (Button)view;
        String num = button.getText().toString();
        if(isClear || isClearNum) {
            textView.append("\n");
            editText.setText(num);
            isClear = false;
            isClearNum = false;
            result.setLength(0);
        }
        else if(result.length() > 0 && isPorE(result) || result.charAt(result.length() - 1) == ')'
                || (num.equals("e") && Character.isDigit(result.charAt(result.length() - 1)))){
            result.append('*');
            editText.append("*");
            editText.append(num);
        }
        else {
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
