package com.example.notes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.notes.R;
import com.example.notes.models.ListItem;
import com.example.notes.models.Note;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private LayoutInflater inflater;
    private int layoutResourceId;
    private List<Note> notes;

    public NoteAdapter(Context context, int resource, List<Note> notes) {
        super(context, resource, notes);
        this.notes = notes;
        this.layoutResourceId = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    public View getView(int pos, View convertView, @NotNull ViewGroup parent) {
        //view holder
        ListItem item;
        if(convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            item = new ListItem();
            item.bodyView = convertView.findViewById(R.id.bodyView);
            item.tagsView = convertView.findViewById(R.id.tagsView);
            item.titleView = convertView.findViewById(R.id.titleView);
            convertView.setTag(item);
        }
        else {
            item = (ListItem) convertView.getTag();
        }

        Note note = notes.get(pos);

        item.titleView.setText(note.getTitle());
        item.tagsView.setText(note.getSringTags());
        item.bodyView.setText(note.getBody());

        return convertView;
    }
}
