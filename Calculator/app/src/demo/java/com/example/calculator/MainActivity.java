package com.example.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ScrollView mainScrollView;
    EditText editText;
    Button deleteButton;

    SharedPreferences preferences;
    static final String APP_PREFERECNES = "settings";
    boolean isClear = true;
    boolean isInvalid = false;
    //boolean isClearNum = true;
    StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout layout = findViewById(R.id.baseFragment);
        Button button = findViewById(R.id.scl);
        layout.removeView(button);
        Button eqB = findViewById(R.id.equals);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(eqB.getLayoutParams());
        params.columnSpec = GridLayout.spec(0, 4, 1);
        eqB.setLayoutParams(params);

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
        //isClearNum = preferences.getBoolean("isClearNum", true);
    }
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor ed =preferences.edit();
        ed.putString("textView", textView.getText().toString());
        ed.putString("editText", editText.getText().toString());
        ed.putString("result", result.toString());
        ed.putBoolean("isClear", isClear);
        //ed.putBoolean("isClearNum", isClearNum);
        ed.apply();
    }

    protected void clearEdit(){
        isInvalid = false;
        result.setLength(0);
        editText.setText("");
    }

    public void onOpClick(View view)
    {
        //isClearNum = false;
        if(isInvalid)
            clearEdit();
        Button btn = (Button)view;
        String op = btn.getText().toString();
        switch (op)
        {
            case "del":
                Editable txt = editText.getText();
                if(editText.length() > 0)
                    editText.setText(txt.delete(txt.length() - 1, txt.length()));
                else
                    isClear = true;
                if(result.length() > 0)
                    result.setLength(result.length() - 1);
                else
                    isClear = true;
                return;
            case "=":
                String res = Calculator.exec(result.toString());
                if (!Character.isDigit(res.charAt(0))
                        && res.charAt(0) != '-') {
                    isInvalid = true;
                }
                textView.append(editText.getText());
                StringBuilder res_text = new StringBuilder(res);
                editText.setText(res_text);
                result = new StringBuilder(res);
                textView.append("\n");
                //isClearNum = true;
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
        if (isInvalid)
            clearEdit();
        Button button = (Button)view;
        String num = button.getText().toString();
        if(isClear) {
            textView.append("\n");
            editText.setText(num);
            isClear = false;
            //isClearNum = false;
            result.setLength(0);
        }
        else {
            editText.append(num);
        }
        result.append(num);
    }

}
