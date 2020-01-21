package com.example.notes.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.models.Note;
import com.example.notes.models.SortEnum;

public class MainActivity extends AppCompatActivity {
    public interface OnActivitySortListener {
        void onActivitySortListener(SortEnum sortType);
    }
    public interface OnActivityChangeAscending {
        void onActivityChangeAscending(boolean direction);
    }
    public interface OnActivityFindListener {
        void onActivityFindListener(String tag);
    }

    private OnActivitySortListener sortListener;
    private OnActivityFindListener findListener;
    private OnActivityChangeAscending changeAscending;
    private EditText findEdit;
    private Switch sortSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.notesListFragment);

        sortListener = (OnActivitySortListener) fragment;
        findListener = (OnActivityFindListener) fragment;
        changeAscending = (OnActivityChangeAscending) fragment;

        Spinner spinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<SortEnum> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SortEnum.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortEnum sortType = (SortEnum) parent.getItemAtPosition(position);
                sortListener.onActivitySortListener(sortType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        findEdit = findViewById(R.id.tagFindEdit);
        findEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                findListener.onActivityFindListener(editable.toString());
            }
        });
        findEdit.clearFocus();

        sortSwitch = findViewById(R.id.switchSearch);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                changeAscending.onActivityChangeAscending(isChecked);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        findEdit.clearFocus();
    }

    public void addButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra("note", new Note());
        startActivity(intent);
    }
}
