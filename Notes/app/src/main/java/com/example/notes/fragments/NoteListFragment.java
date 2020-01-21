package com.example.notes.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.notes.R;
import com.example.notes.activitys.MainActivity;
import com.example.notes.activitys.NoteActivity;
import com.example.notes.adapters.DBAdapter;
import com.example.notes.adapters.NoteAdapter;
import com.example.notes.models.Note;
import com.example.notes.models.SortEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NoteListFragment extends Fragment
        implements MainActivity.OnActivitySortListener, MainActivity.OnActivityFindListener,
        MainActivity.OnActivityChangeAscending{

    private List<Note> notes = new ArrayList<>();
    private AdapterView adapterView;
    private NoteAdapter noteAdapter;
    private SortEnum sortType = SortEnum.Date;
    private boolean isAscending = false;
    private String tagsForSearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        adapterView = view.findViewById(R.id.notesList);

        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NoteActivity.class);
                intent.putExtra("note", notes.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        notes = dbAdapter.getNotes();
        sortNotes();
        if(!tagsForSearch.equals(""))
            filterNotesByTag();

        noteAdapter = new NoteAdapter(getContext(), R.layout.note, notes);
        adapterView = getView().findViewById(R.id.notesList);
        adapterView.setAdapter(noteAdapter);
        dbAdapter.close();
    }

    @Override
    public void onActivitySortListener (SortEnum sortType) {
        this.sortType = sortType;
        sortNotes();
        if(noteAdapter != null)
            noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityFindListener (String tags) {
        tagsForSearch = tags;
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        notes.clear();
        notes.addAll(dbAdapter.getNotes());
        filterNotesByTag();
        dbAdapter.close();
        if(noteAdapter != null)
            noteAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityChangeAscending(boolean direction)
    {
        this.isAscending = direction;
        sortNotes();
        if(noteAdapter != null)
            noteAdapter.notifyDataSetChanged();
    }

    private void sortNotes() {
        if(sortType.toString().equals(SortEnum.Date.toString())) {
            if(isAscending)
            {
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note1.getCreatingDate().compareTo(note2.getCreatingDate()); }
                });
            }
            else {
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note2.getCreatingDate().compareTo(note1.getCreatingDate()); }
                });
            }
        }
        else {
            if(isAscending) {
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note1.getTitle().toLowerCase().
                                compareTo(note2.getTitle().toLowerCase());
                    }
                });
            }
            else {
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note2.getTitle().toLowerCase().
                                compareTo(note1.getTitle().toLowerCase());
                    }
                });
            }
        }
    }

    private void filterNotesByTag() {
        if (tagsForSearch.equals("")) {
            sortNotes();
            return;
        }
        List<String> tmpTags = Arrays.asList(tagsForSearch.split(" "));
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note: notes) {
            Boolean flg = true;
            for(String tag : tmpTags) {
                if (note.getTags() != null) {
                    if (!note.getTags().contains(tag)) {
                        flg = false;
                        break;
                    }
                }
            }
            if(flg)
                filteredNotes.add(note);
        }
        notes.clear();
        notes.addAll(filteredNotes);
        sortNotes();
    }
}