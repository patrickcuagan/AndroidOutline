package com.example.patrick.outline;
import java.util.Date;


/**
 * Created by Patrick on 3/14/17.
 */

public class Note {

    public static final String TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DELETED = "deleted";

    private int id;
    private String text;
    private String date_accessed;
    private int isDeleted;

    public Note() {
    }

    public Note(String text) {
        this.text = text;
    }

    public Note(String text, String date_accessed) {
        this.text = text;
        this.date_accessed = date_accessed;
    }

    public Note(int id, String text, String date_accessed) {
        this.id = id;
        this.text = text;
        this.date_accessed = date_accessed;
    }

    public Note(String text, String date_accessed, int isDeleted) {
        this.text = text;
        this.date_accessed = date_accessed;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate_accessed() {
        return date_accessed;
    }

    public void setDate_accessed(String date_created) {
        this.date_accessed = date_created;
    }

    public int getDeleted() {
        return isDeleted;
    }

    public void setDeleted(int deleted) {
        isDeleted = deleted;
    }
}
