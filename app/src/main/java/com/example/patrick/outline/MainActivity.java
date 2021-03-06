package com.example.patrick.outline;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // KEYS

    static final int PICTURE_TAKEN = 1;
    private static final String TWITTER_KEY = "wlfGE1ohYo8vbDTwRSt2oJC1C";
    private static final String TWITTER_SECRET = "ulJ6BbYBvbIq7eVYsNzV6iE1RnFSdzOvhOMqLTMkGqYlXAkdAt";

    // ELEMENTS

    DrawerLayout drawerLayout;
    RelativeLayout rlMain;
    RecyclerView rvNote;
    ViewPager viewPager;
    TabLayout tabLayout;

    NoteAdapter noteAdapter;
    DatabaseHelper dbHelper;

    CountDownTimer timer;
    boolean isRunning = false;
    boolean isCreated = false;

    EditText etText;
    TextView tvId;
    ImageButton ibSubmit, ibSubmitInDrawer, ibDrawer, ibCloseDrawer, ibSettings, ibSettings2;

    FloatingActionButton fabCamera;

    ImageView ivImage;

    /*
        Just to be able to close navigationview with back press on system
     */
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
        For the image is taken;
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_TAKEN && resultCode == RESULT_OK){

            String imagePath = data.getStringExtra("imagePath");

            if(!imagePath.equals("-1")) {
                Image image = new Image(1, data.getStringExtra("imagePath"));
                dbHelper.createImage(image);

                // Get path of image
                Log.d("Main", imagePath);

                // Drawable drawable = Drawable.createFromPath("/storage/emulated/0/pic.jpg");
                Drawable drawable = Drawable.createFromPath(imagePath);
                ivImage = (ImageView) findViewById(R.id.iv_image);
                ivImage.setImageDrawable(drawable);

                /*
                // Create image bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                ImageView view = new ImageView(getBaseContext());
                RelativeLayout.LayoutParams lp =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlMain.addView(view, lp);
                */
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(etText.getText().toString().trim().length() > 0 && !isCreated) {
            DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());

            String noteText = etText.getText().toString();
            Note note = new Note(noteText, getDateTime());

            dbHelper.createNote(note);
        }
    }

    /*
        When the application is "minimized", it starts the countdown
        If countdown finishes, save the db if there is something in it
        ONRESUME CANCELS COUNTDOWN!!!!
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int timeToRefresh = sp.getInt("timeToRefresh", -1);

        if(timeToRefresh != -2) {
            timer = new CountDownTimer(timeToRefresh, 1000) {
                public void onTick(long millisUntilFinished) {
                    isRunning = true;
                }

                public void onFinish() {
                    if(etText.getText().toString().trim().length() > 0) {
                        isCreated = true;
                        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());

                        String noteText = etText.getText().toString();
                        Note note = new Note(noteText, getDateTime());

                        dbHelper.createNote(note);

                        etText.setText("");
                        noteAdapter.changeCursor(dbHelper.getAllNotes());
                    }
                }
            }.start();
        }
    }

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
        // BINAGO ko 2 LINES BELOW //
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rlMain = (RelativeLayout) findViewById(R.id.rl_main);

        Drawable drawable = Drawable.createFromPath("/storage/emulated/0/pic.jpg");
        ivImage = (ImageView) findViewById(R.id.iv_image);
        ivImage.setImageDrawable(drawable);

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

        /*
            Navigation view -- open close navigation view with buttons!
                            -- if tab is notes, get all notes .. if tabs is deleted, get all deleted notes
         */
        ibDrawer = (ImageButton) findViewById(R.id.ib_drawer);
        ibDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        ibCloseDrawer = (ImageButton) findViewById(R.id.ib_close_drawer);
        ibCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Notes"));
        tabLayout.addTab(tabLayout.newTab().setText("Deleted"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    // notes
                    noteAdapter.changeCursor(dbHelper.getAllNotes());
                } else {
                    // deleted
                    noteAdapter.changeCursor(dbHelper.getAllDeletedNotes());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /*
            Add -- adds a new note and just sets edittext to blank and notifies (so navigationview can be updated)
                -- better not do tell user that he cannot add empty text because might be too disturbing
         */
        etText = (EditText) findViewById(R.id.et_text);
        tvId = (TextView) findViewById(R.id.tv_hidden_id);

        ibSubmit = (ImageButton) findViewById(R.id.ib_submit);
        ibSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        ibSubmitInDrawer = (ImageButton) findViewById(R.id.ib_submit_in_drawer);
        ibSubmitInDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                addNote();
                tvId.setText("-1"); // because you're adding -- reset the id to -1 again
            }
        });

        /*
            Edit -- if there is an intent (one of the entered notes was clicked), display it and update the time
         */
        if (getIntent().getExtras() != null) {
            int id = getIntent().getIntExtra(Note.COLUMN_ID, -1);

            if (id != -1) {
                DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                Note note = dbHelper.getNote(id);

                // set date accessed to date right now
                note.setDate_accessed(getDateTime());
                dbHelper.updateNote(note);
                noteAdapter.changeCursor(dbHelper.getAllNotes());

                etText.setText(note.getText());
                tvId.setText(note.getId() + "");
            }
        }

        /*
            Delete with swipe actions!
         */


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //HERE IS THE TRICK
                    viewHolder.itemView.setTranslationX(dX);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                            actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {    //if swipe left

                    // alert for confirm to delete 
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    // set message  
                    builder.setMessage("Are you sure to delete?");
                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {

                        // when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RecyclerView.ViewHolder view = rvNote.findViewHolderForLayoutPosition(position);
                            TextView a = (TextView) view.itemView.findViewById(R.id.tv_id);

                            int note_id = Integer.parseInt(a.getText().toString());
                            noteAdapter.getCursor().moveToPosition(note_id);

                            //   Deleting from notes takes it to deleted tab
                            //   Deleting form deleted permanently deletes note

                            final DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                            if (tabLayout.getSelectedTabPosition() == 0) {
                                final Note note = dbHelper.getNote(note_id);
                                note.setDeleted(1);
                                dbHelper.toggleDeleteNote(note);
                                final Cursor oldCursor = noteAdapter.swapCursor(dbHelper.getAllNotes());
                                Snackbar.make(rvNote, "Note moved to delete pane.", Snackbar.LENGTH_LONG).
                                        setAction("Undo", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                note.setDeleted(0);
                                                dbHelper.toggleDeleteNote(note);
                                                noteAdapter.changeCursor(oldCursor);
                                                noteAdapter.notifyDataSetChanged();
                                            }

                                        }).show();
                            } else {
                                dbHelper.permanentDeleteNote(note_id);
                                noteAdapter.changeCursor(dbHelper.getAllDeletedNotes());
                                Snackbar.make(rvNote, "Note deleted permanently.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (tabLayout.getSelectedTabPosition() == 0) {
                                noteAdapter.changeCursor(dbHelper.getAllNotes());
                            } else {
                                noteAdapter.changeCursor(dbHelper.getAllDeletedNotes());
                            }
                            return;
                        }
                    }).show();  // show alert dialog
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvNote);

        // Toggle settings
        //
        ibSettings=(ImageButton)
                findViewById(R.id.ib_settings);

        ibSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        ibSettings2=(ImageButton)
                findViewById(R.id.ib_settings);

        ibSettings2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(i);
            }
        });


                /*
                    Camera Action
                 */
        fabCamera=(FloatingActionButton)

                findViewById(R.id.fab);

        fabCamera.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick (View view){
                                             Intent i = new Intent(getBaseContext(), CameraActivity.class);
                                             startActivity(i);
                                         }
                                     }

        );
    }

    // Get date and time in string

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addNote() {
        if (etText.getText().toString().trim().length() > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
            int id = Integer.parseInt(tvId.getText().toString());
            String noteText = etText.getText().toString();
            Note note;

            if (dbHelper.doesNoteExist(id)) {
                note = dbHelper.getNote(id);
                if (!note.getText().equals(noteText) && note.getDeleted() != 1) {
                    note.setText(noteText);
                    note.setDate_accessed(getDateTime());
                    dbHelper.updateNote(note);

                    note.setDeleted(0);
                    dbHelper.toggleDeleteNote(note);
                }
            } else {
                note = new Note(noteText, getDateTime(), 0);
                dbHelper.createNote(note);
            }

            etText.setText("");
            noteAdapter.changeCursor(dbHelper.getAllNotes());
        }
    }

}

// steps
// add note --> click drawer --> click note ---> add new note in drawer --> submit gone!

