package com.githubv3api.meesn.githubv3api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.githubv3api.meesn.githubv3api.Adapter.UserRecyclerAdapter;
import com.githubv3api.meesn.githubv3api.model.User;
import com.githubv3api.meesn.githubv3api.service.UserClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReposList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String baseUrl = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos_list);
        getSupportActionBar().setTitle("Repositories");

        //Recycler View
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserRecyclerAdapter userRecyclerAdapter = new UserRecyclerAdapter();
        recyclerView.setAdapter(userRecyclerAdapter);


        //Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient client = retrofit.create(UserClient.class);
        String userLoginName = getIntent().getStringExtra("userLoginName");
        Call<List<User>> call = client.reposForUser(userLoginName);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> repos = response.body();
                userRecyclerAdapter.setUsers(repos);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ReposList.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
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
