package com.example.notes.activitys;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.R;
import com.example.notes.adapters.DBAdapter;
import com.example.notes.models.Note;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    private EditText titleEdit;
    private EditText tagsEdit;
    private EditText bodyEdit;
    private DBAdapter dbAdapter;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleEdit = findViewById(R.id.titleEdit);
        tagsEdit = findViewById(R.id.tagsEdit);
        bodyEdit = findViewById(R.id.bodyEdit);
        dbAdapter = new DBAdapter(this);

        fillFields();
    }

    private void fillFields() {
        Bundle arguments = getIntent().getExtras();

        note = (Note)arguments.getSerializable("note");

        if(note.hasTitle())
            titleEdit.setText(note.getTitle());
        tagsEdit.setText(note.getSringTags());
        bodyEdit.setText(note.getBody());
    }

    public void save(View view) {
        String title = titleEdit.getText().toString();
        String tags = tagsEdit.getText().toString();
        String body = bodyEdit.getText().toString();
        String date = note.getStringDate();
        if(body.equals("")) {
            showError();
            return;
        }

        Note newNote = new Note(note.getId(), title, body, tags, date);
        dbAdapter.open();
        if(note.getId() != 0)
            dbAdapter.update(newNote);
        else
            dbAdapter.insert(newNote);

        dbAdapter.close();
        this.onBackPressed();
    }

    private void showError() {
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.bodyNotFilledError,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void delete(View view) {
        if(note.getId() != 0)
            this.onBackPressed();

        dbAdapter.open();
        dbAdapter.delete(note.getId());
        dbAdapter.close();
        this.onBackPressed();
    }
}
