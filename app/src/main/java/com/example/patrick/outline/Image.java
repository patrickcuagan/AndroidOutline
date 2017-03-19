package com.example.patrick.outline;

/**
 * Created by jasonsapdos on 19/03/2017.
 */

public class Image {

    public static final String TABLE = "image";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDNOTE = "idnote";
    public static final String COLUMN_PATH = "path";

    private int id; // Image ID
    private int idNote; // Note where the image will be shown
    private String imagePath; // Path of the image

    public Image() {
    }

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image(int idNote, String imagePath) {
        this.idNote = idNote;
        this.imagePath = imagePath;
    }

    public Image(int id, int idNote, String imagePath) {
        this.id = id;
        this.idNote = idNote;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
