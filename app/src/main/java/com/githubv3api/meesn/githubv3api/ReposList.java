package com.githubv3api.meesn.githubv3api;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.githubv3api.meesn.githubv3api.Adapter.UserRecyclerAdapter;
import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.model.User;
import com.githubv3api.meesn.githubv3api.service.UserClient;
import com.githubv3api.meesn.githubv3api.viewmodel.AppViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String baseUrl = "https://api.github.com";
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrolledOutItems;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private ImageView imageView;
    private AppViewModel appViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos_list);
        getSupportActionBar().setTitle("Repositories");

        linearLayoutManager = new LinearLayoutManager(this);
        progressBar = findViewById(R.id.loadmore);
        imageView = findViewById(R.id.bin);

        //Recycler View
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        final UserRecyclerAdapter userRecyclerAdapter = new UserRecyclerAdapter();
        recyclerView.setAdapter(userRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems+scrolledOutItems == totalItems))
                {
                    //Data Fetch
                    isScrolling = false;
                    fetchData();
                    Log.d("Fetch", "Fetching");
                }
            }
        });

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appViewModel.loadRepositories(getIntent().getStringExtra("userLoginName"));
            }
        });
        if (appViewModel.getRepositories() != null)
        {
            appViewModel.getRepositories().observe(this, new Observer<List<Repository>>() {
                @Override
                public void onChanged(@Nullable List<Repository> repositories) {
                    if (repositories != null && !repositories.isEmpty())
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        userRecyclerAdapter.setUsers(repositories);
                    }
                    else
                    {
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        },2000);
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
            Intent intent = new Intent(ReposList.this, Login.class);
            startActivity(intent);
            ReposList.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
