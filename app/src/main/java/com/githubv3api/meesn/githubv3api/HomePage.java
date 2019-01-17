package com.githubv3api.meesn.githubv3api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.ui.BrowseRepositories;
import com.githubv3api.meesn.githubv3api.ui.MyRepositories;

public class HomePage extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment currentFragment = null;
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MyRepositories myRepositories = new MyRepositories();
                    manager.beginTransaction().replace(R.id.fragment_frames,myRepositories,myRepositories.getTag()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    BrowseRepositories browseRepositories = new BrowseRepositories();
                    manager.beginTransaction().replace(R.id.fragment_frames,browseRepositories,browseRepositories.getTag()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().setTitle("Repositories");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager manager = getSupportFragmentManager();
        MyRepositories myRepositories = new MyRepositories();
        manager.beginTransaction().replace(R.id.fragment_frames,myRepositories,myRepositories.getTag()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.logout)
        {
            SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFS",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(HomePage.this, Login.class);
            startActivity(intent);
            HomePage.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

}
