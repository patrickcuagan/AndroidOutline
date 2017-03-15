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

    private int id;
    private String text;
    private String date_created;

    public Note() {
    }

    public Note(String text) {
        this.text = text;
    }

    public Note(String text, String date_created) {
        this.text = text;
        this.date_created = date_created;
    }

    public Note(int id, String text, String date_created) {
        this.id = id;
        this.text = text;
        this.date_created = date_created;
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

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
