package com.example.notes.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.interfaces.OnActivityFindListener;
import com.example.notes.interfaces.OnActivitySortListener;
import com.example.notes.models.Note;
import com.example.notes.models.SortEnum;

public class MainActivity extends AppCompatActivity {
    private OnActivitySortListener sortListener;
    private OnActivityFindListener findListener;
    private EditText findEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.notesListFragment);
        sortListener = (OnActivitySortListener) fragment;
        findListener = (OnActivityFindListener) fragment;

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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                findListener.onActivityFindListener(editable.toString());
            }
        });
        findEdit.clearFocus();
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
