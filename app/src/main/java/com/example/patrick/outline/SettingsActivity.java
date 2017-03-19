package com.example.patrick.outline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class SettingsActivity extends AppCompatActivity {
    boolean clicked30s, clicked1m, clicked5m, clickedNever = false;
    Button btnDone;
    private TwitterLoginButton loginButton;
    private static final String TWITTER_KEY = "KAwucHyA7xg7x1qh5J0jItJkv";
    private static final String TWITTER_SECRET = "kHdYvf08P3BUqStR4ytra4dDY5O8pBNYMkqEK1FP8dfzhWijMt";
    Button btn30s, btn1m, btn5m, btnNever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        btn30s = (Button) findViewById(R.id.btn_30s);
        btn1m = (Button) findViewById(R.id.btn_1m);
        btn5m = (Button) findViewById(R.id.btn_5m);
        btnNever = (Button) findViewById(R.id.btn_never);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                Log.d("twitter", "success");
                //Twitter.getInstance().core.getSessionManager().getActiveSession();
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(sp.contains("timer")){
            //stay and then set the TextView
            String timer = sp.getString("timer", null);
            Log.d("check", timer+"");
            if(timer.equals("btn30s")){
                Log.d("30SECONDS", timer+"");
                btn30s.setBackgroundColor(getResources().getColor(R.color.Gray));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));
            }
            else if(timer.equals("btn1m")){
                Log.d("check", timer+"");
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.Gray));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));
            }
            else if(timer.equals("btn5m")){
                Log.d("check", timer+"");
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.Gray));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));
            }
            else if(timer.equals("btnNever")){
                Log.d("check", timer+"");
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.Gray));
            }
        }
        /*
            Save settings
         */
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        /*
            Time it takes to refresh settings
         */

        btn30s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();
                Log.d("30s", "WEW");
                editor.putInt("timeToRefresh", 30000);
                editor.putString("timer", "btn30s");
                editor.commit();
                btn30s.setBackgroundColor(getResources().getColor(R.color.Gray));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));
            }
        });

        btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.Gray));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));
                editor.putInt("timeToRefresh", 60000);
                editor.putString("timer", "btn1m");
                editor.commit();

            }
        });

        btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.Gray));
                btnNever.setBackgroundColor(getResources().getColor(R.color.White));

                editor.putInt("timeToRefresh", 300000);
                editor.putString("timer", "btn5m");
                editor.commit();

            }
        });

        btnNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sp.edit();
                btn30s.setBackgroundColor(getResources().getColor(R.color.White));
                btn1m.setBackgroundColor(getResources().getColor(R.color.White));
                btn5m.setBackgroundColor(getResources().getColor(R.color.White));
                btnNever.setBackgroundColor(getResources().getColor(R.color.Gray));
                editor.putInt("timeToRefresh", -2);
                editor.putString("timer","btnNever");
                editor.commit();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

