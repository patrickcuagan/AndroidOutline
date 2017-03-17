package com.example.patrick.outline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button btnDone;

    Button btn30s, btn1m, btn5m, btnNever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*
            Save settings
         */
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });


        /*
            Time it takes to refresh settings
         */
        btn30s = (Button) findViewById(R.id.btn_30s);
        btn1m = (Button) findViewById(R.id.btn_1m);
        btn5m = (Button) findViewById(R.id.btn_5m);
        btnNever = (Button) findViewById(R.id.btn_never);

        btn30s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("timeToRefresh", 30000);
                editor.commit();
            }
        });

        btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("timeToRefresh", 60000);
                editor.commit();
            }
        });

        btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("timeToRefresh", 300000);
                editor.commit();
            }
        });

        btnNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("timeToRefresh", -2);
                editor.commit();
            }
        });
    }
}
