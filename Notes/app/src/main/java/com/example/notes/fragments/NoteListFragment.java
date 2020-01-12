package com.example.notes.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.notes.R;
import com.example.notes.activitys.NoteActivity;
import com.example.notes.adapters.DBAdapter;
import com.example.notes.adapters.NoteAdapter;
import com.example.notes.interfaces.OnActivityFindListener;
import com.example.notes.interfaces.OnActivitySortListener;
import com.example.notes.models.Note;
import com.example.notes.models.SortEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment
        implements OnActivitySortListener, OnActivityFindListener {

    private List<Note> notes = new ArrayList<>();
    private AdapterView adapterView;
    private NoteAdapter noteAdapter;
    private SortEnum sortType = SortEnum.Date;
    private String tagsForSearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        sortType = SortEnum.Date;
        sortNotes();
        if(!tagsForSearch.equals(""))
            filterNotesByTag();

        noteAdapter = new NoteAdapter(getContext(), R.layout.note, notes);
        adapterView = getView().findViewById(R.id.notesList);
        adapterView.setAdapter(noteAdapter);
        dbAdapter.close();
    }

    private void filterNotesByTag() {
        if(tagsForSearch.equals("")) {
            sortNotes();
            return;
        }
        List<String> tmpTags = Arrays.asList(tagsForSearch.split(" "));
        List<Note> filteredNotes = new ArrayList<>();
        for(Note note :notes) {
            Boolean flag = true;
            for(String tag : tmpTags) {
                if(note.getTags() != null && ! note.getTags().contains(tag)) {
                    flag = false;
                    break;
                }
            }
            if(flag) filteredNotes.add(note);
        }
        sortNotes();
        notes.clear();
        notes.addAll(filteredNotes);
    }

    private void sortNotes() {
        if(sortType.toString().equals(SortEnum.Date.toString())) {
            notes.sort(new Comparator<Note>() {
                @Override
                public int compare(Note n1, Note n2) {
                    return n2.getCreatingDate().compareTo(n1.getCreatingDate());
                }
            });
        }
        else {
            notes.sort(new Comparator<Note>() {
                @Override
                public int compare(Note n1, Note n2) {
                    return n1.getTitle().toLowerCase().compareTo(n2.toString().toLowerCase());
                }
            });
        }
    }

    @Override
    public void onActivitySortListener (SortEnum sortType) {
        this.sortType = sortType;
        sortNotes();
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityFindListener (String tag) {
        tagsForSearch = tag;
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        notes.clear();
        notes.addAll(dbAdapter.getNotes());
        filterNotesByTag();
        dbAdapter.close();
        if(notes != null)
            noteAdapter.notifyDataSetChanged();
    }
}
