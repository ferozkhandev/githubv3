package com.githubv3api.meesn.githubv3api.apprepository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.githubv3api.meesn.githubv3api.Login;
import com.githubv3api.meesn.githubv3api.ReposList;
import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.database.RepositoryDB;
import com.githubv3api.meesn.githubv3api.model.User;
import com.githubv3api.meesn.githubv3api.service.UserClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppRepository {
    private static AppRepository ourInstance;
    private RepositoryDB repositoryDB;
    private Executor executor = Executors.newSingleThreadExecutor();
    private LiveData<List<Repository>> repositories;
    private String username, password;
    String baseUrl = "https://api.github.com";

    //Retrofit Builder Initialization
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = retrofitBuilder.build();
    private Context context;
    private boolean loginSuccess = false;
    private String userLoginName;

    public static AppRepository getInstance(Context context) {
        ourInstance = new AppRepository(context);
        return ourInstance;
    }

    private AppRepository(Context context) {
        repositoryDB = RepositoryDB.getInstance(context);
    }

    public void addRepository(final Repository repository)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repositoryDB.repositoryDAO().addRepository(repository);
            }
        });
    }

    public void addRepository(final List<Repository> repository)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repositoryDB.repositoryDAO().addRepository(repository);
            }
        });
    }

    public synchronized void loadRepositories(String userLoginName)
    {
        //Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient client = retrofit.create(UserClient.class);
        Call<List<Repository>> call = client.reposForUser(userLoginName);
        Log.d("CheckUserName", userLoginName);

        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                final List<Repository> repos = response.body();
                if (repos != null && !repos.isEmpty())
                {
                    for (Repository rep: repos) {
                        rep.setRepoType("userrepo");
                    }
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            repositoryDB.repositoryDAO().addRepository(repos);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                Log.d("Error", "error :(");
                //Toast.makeText(ReposList.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<List<Repository>> getRepositories()
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repositories = repositoryDB.repositoryDAO().getRepositories();
            }
        });
        return repositories;
    }


    public void loginUser(String username, String password, Context context)
    {
        AppRepository.this.username = username;
        AppRepository.this.password = password;
        AppRepository.this.context = context;

        new UserLoginTask().execute();
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public boolean getLoginStatus()
    {
        return loginSuccess;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            UserClient userClient = retrofit.create(UserClient.class);
            String username = AppRepository.this.username;
            String password = AppRepository.this.password;
            String base = username + ":" + password;
            String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
            Call<User> call = userClient.userlogin(authHeader);
            try {
                Response<User> response = call.execute();
                if (response.isSuccessful()) {
                    Log.d("userlogin", "Login Done");
                    userLoginName = response.body().getLogin();
                    return true;
                } else {
                    Log.d("userlogin", response.message());
                }

            } catch (IOException ex) {
                Log.d("userlogin", ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                loginSuccess = true;
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
