package com.githubv3api.meesn.githubv3api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private int splashInterval = 2000;
    private boolean alreadylogin = false;
    private String userLoginName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (readsp())
                {
                    Intent i = new Intent(SplashActivity.this, ReposList.class);
                    i.putExtra("userLoginName", userLoginName);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, Login.class);
                    startActivity(i);
                }
                SplashActivity.this.finish();
            }
            private void finish() {
            }}, splashInterval);
    };

    private boolean readsp()
    {
        SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        alreadylogin = sharedPref.getBoolean("alreadylogin", false);
        userLoginName = sharedPref.getString("userloginname", null);
        Log.d("readssharedpreference", String.valueOf(alreadylogin)+" "+String.valueOf(userLoginName));
        return alreadylogin;
    }
}
