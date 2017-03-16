package com.example.patrick.outline;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView rvNote;
    NoteAdapter noteAdapter;
    DatabaseHelper dbHelper;

    CountDownTimer timer;
    boolean isRunning = false;
    boolean isCreated = false;

    EditText etText;
    TextView tvId;
    ImageButton ibSubmit, ibDrawer;

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(etText.getText().toString().trim().length() > 0 && !isCreated) {
            Log.d("debug", "DESTROY");
            DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());

            String noteText = etText.getText().toString();
            Note note = new Note(noteText, getDateTime());

            dbHelper.createNote(note);
        }
    }

    // when the application is "minimized", it countdowns.. based on settings!!!!!
    // and then save to db if ever the time passes
    @Override
    protected void onPause() {
        super.onPause();

        timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                isRunning = true;
            }

            public void onFinish() {
                if(etText.getText().toString().trim().length() > 0) {
                    isCreated = true;
                    Log.d("debug", "PAUSE");
                    DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());

                    String noteText = etText.getText().toString();
                    Note note = new Note(noteText, getDateTime());

                    dbHelper.createNote(note);

                    finish();
                }
            }
        }.start();
    }

    // related to onPause --- once the application resumes, stop the timer and proceed as is
    @Override
    protected void onResume() {
        super.onResume();
        if(isRunning) {
            timer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        rvNote = (RecyclerView) findViewById(R.id.rv_note);
        dbHelper = new DatabaseHelper(getBaseContext());
        noteAdapter = new NoteAdapter(getBaseContext(), dbHelper.getAllNotes());

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra(Note.COLUMN_ID, id);
                startActivity(i);
            }
        });

        rvNote.setHasFixedSize(true);
        rvNote.setAdapter(noteAdapter);
        rvNote.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));

        // Toggles the NavigationView Drawer
        ibDrawer = (ImageButton) findViewById(R.id.ib_drawer);
        ibDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        // ADD
        // Adds a new note and creates a new activity (blank text file once again)
        // If there is no text edited, nothing will happen (flashing of alerts = somewhat disturbing)
        etText = (EditText) findViewById(R.id.et_text);
        tvId = (TextView) findViewById(R.id.tv_hidden_id);
        ibSubmit = (ImageButton) findViewById(R.id.ib_submit);

        ibSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etText.getText().toString().trim().length() > 0) {
                    DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                    int id = Integer.parseInt(tvId.getText().toString());
                    String noteText = etText.getText().toString();
                    Note note;

                    if(dbHelper.doesNoteExist(id)) {
                        note = dbHelper.getNote(id);
                        note.setText(noteText);
                        dbHelper.updateNote(note);
                    } else {
                        note = new Note(noteText, getDateTime());
                        dbHelper.createNote(note);
                    }

                    etText.setText("");
                    noteAdapter = new NoteAdapter(getBaseContext(), dbHelper.getAllNotes());
                    noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int id) {
                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            i.putExtra(Note.COLUMN_ID, id);
                            startActivity(i);
                        }
                    });
                    rvNote.setAdapter(noteAdapter);
                }
            }
        });

        // EDIT
        // If there is an intent (one of the entered notes was clicked), display it
        if(getIntent().getExtras() != null) {
            int id = getIntent().getIntExtra(Note.COLUMN_ID, -1);

            if(id != -1) {
                DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                Note note = dbHelper.getNote(id);

                etText.setText(note.getText());
                tvId.setText(note.getId()+"");
            }
        }

        // DELETE
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                RecyclerView.ViewHolder view = rvNote.findViewHolderForLayoutPosition(position);
                TextView a = (TextView) view.itemView.findViewById(R.id.tv_id);

                DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                dbHelper.deleteNote(Integer.parseInt(a.getText().toString()));

                noteAdapter = new NoteAdapter(getBaseContext(), dbHelper.getAllNotes());
                noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int id) {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtra(Note.COLUMN_ID, id);
                        startActivity(i);
                    }
                });
                rvNote.setAdapter(noteAdapter);

            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvNote);

    }

    // Get date and time in string
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}

// bugs:
// 1. deleting does not animate properly