package com.example.notes.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Note implements Serializable {
    private long id;
    private String title;
    private String body;
    private Date creatingDate;
    private List<String> tags;

    public Note(){
        id = 0;
        title = "";
        body = "";
        creatingDate = new Date();
        tags = null;
    }
    public Note(long id, String title, String body, String tags)
    {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tags = Arrays.asList(tags.toLowerCase().split(" "));
        creatingDate = new Date();
    }

    public Note(long id, String title, String body, String tags, String date)
    {
        this(id, title, body, tags);
        try {
            this.creatingDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
        }
        catch (ParseException ex)
        {
            this.creatingDate = new Date();
        }
    }
    public long getId() {
        return id;
    }
    public String getTitle() {
        if(title.equals(""))
            return getStringDate();
        return title;
    }
    public String getBody() {
        return body;
    }
    public Date getCreatingDate() {
        return creatingDate;
    }
    public String getSringTags(){
        if(tags == null) return "";
        return String.join(" ", tags);
    }

    public List<String> getTags() {
        return tags;
    }
    public boolean hasTitle() {
        return !title.equals("");
    }
    public boolean hasTag(String tag) {
        return tags.contains(tag.toLowerCase());
    }
    public String getStringDate()
    {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(creatingDate);
    }
}
