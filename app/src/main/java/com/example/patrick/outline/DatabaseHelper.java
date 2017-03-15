package com.example.patrick.outline;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Patrick on 3/14/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SCHEMA = "note";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, SCHEMA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // if db exists, do nothing
        // create tables here

        String sql = "CREATE TABLE " + Note.TABLE + "( "
                + Note.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Note.COLUMN_TEXT + " TEXT NOT NULL, "
                + Note.COLUMN_DATE + " TEXT);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + Note.TABLE;
        db.execSQL(sql);

        onCreate(db);
    }

    public long createNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Note.COLUMN_TEXT, note.getText());
        cv.put(Note.COLUMN_DATE, note.getDate_created());

        long id = db.insert(Note.TABLE, null, cv);
        db.close();

        return id;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Note.COLUMN_TEXT, note.getText());

        int rows = db.update(Note.TABLE, cv, Note.COLUMN_ID + " =?", new String[] { note.getId() + "" } );

        db.close();

        return rows;
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();

        int rows = db.delete(Note.TABLE, Note.COLUMN_ID+ "=?", new String[]{ id + "" });
        db.close();
        return rows;
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(Note.TABLE, null, null, null, null, null, null);
    }

    public Note getNote(int id) {
        Note note = null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE, null, Note.COLUMN_ID + "=?", new String[] { id + "" }, null, null, null);

        if(cursor.moveToFirst()) {
            note = new Note();
            String text = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TEXT));
            note.setText(text);

            note.setId(id);
        }
        cursor.close();
        db.close();

        return note;
    }
}
