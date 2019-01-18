package com.githubv3api.meesn.githubv3api;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.Adapter.FilesRecyclerAdapter;
import com.githubv3api.meesn.githubv3api.model.File;
import com.githubv3api.meesn.githubv3api.viewmodel.AppViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String baseUrl = "https://api.github.com";
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrolledOutItems;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private ImageView imageView;
    private AppViewModel appViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();
    final FilesRecyclerAdapter filesRecyclerAdapter = new FilesRecyclerAdapter();
    private String username;
    private String repoName;
    private TextView noDataTitle, noDataDescription;
    private InternetCheck internetCheck;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        getSupportActionBar().setTitle("Contents");

        progressDialog = new ProgressDialog(FileList.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        noDataTitle = findViewById(R.id.noDataTitle);
        noDataDescription = findViewById(R.id.noDataDescription);
        internetCheck = new InternetCheck(getApplicationContext());

        username = getIntent().getStringExtra("userLoginName");
        repoName = getIntent().getStringExtra("repoName");

        linearLayoutManager = new LinearLayoutManager(this);
        progressBar = findViewById(R.id.loadmore);
        imageView = findViewById(R.id.bin);

        //Recycler View
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(filesRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    //Data Fetch
                    isScrolling = false;
                    fetchData();
                    Log.d("Fetch", "Fetching");
                }
            }
        });

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        loadData();
    }

    private void loadData() {
        appViewModel.loadFiles(username, repoName, getApplicationContext());
        Log.i("wtferror", username + ":" + repoName);
        if (appViewModel.getFiles() != null) {
            appViewModel.getFiles(repoName).observe(this, new Observer<List<File>>() {
                @Override
                public void onChanged(@Nullable List<File> files) {
                    if (files != null && !files.isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        noDataTitle.setVisibility(View.GONE);
                        noDataDescription.setVisibility(View.GONE);
                        filesRecyclerAdapter.setFiles(files);
                        progressDialog.dismiss();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        noDataTitle.setVisibility(View.VISIBLE);
                        noDataDescription.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },2000);
                        isNetIssue();
                    }
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            noDataTitle.setVisibility(View.VISIBLE);
            noDataDescription.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },2000);
            isNetIssue();
        }
    }

    private void isNetIssue() {
        if (!internetCheck.netCheck()) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isNetIssue();
                        }
                    });
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    isNetIssue();

                }
            }, 5000);
        } else {
            loadData();
        }
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(FileList.this, Login.class);
            startActivity(intent);
            FileList.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
