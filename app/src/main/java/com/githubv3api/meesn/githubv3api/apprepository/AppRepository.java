package com.githubv3api.meesn.githubv3api.apprepository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.githubv3api.meesn.githubv3api.FileList;
import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.database.RepositoryDB;
import com.githubv3api.meesn.githubv3api.model.File;
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
    private String baseUrl = "https://api.github.com";
    private LiveData<List<File>> files;
    private List<User> loadedUsers;
    private boolean checkFuncCall=false;

    //Retrofit Builder Initialization
    private Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create());
    private Retrofit retrofit = retrofitBuilder.build();
    private Context context;
    private boolean loginSuccess = false;
    private String userLoginName;

    public boolean checkLoaded;

    public static AppRepository getInstance(Context context) {
        ourInstance = new AppRepository(context);
        return ourInstance;
    }

    private AppRepository(Context context) {
        repositoryDB = RepositoryDB.getInstance(context);
    }

    public void addRepository(final Repository repository) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repositoryDB.repositoryDAO().addRepository(repository);
            }
        });
    }

    public void addRepository(final List<Repository> repository) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repositoryDB.repositoryDAO().addRepository(repository);
            }
        });
    }

    /*-------------------------------------Repositories Info--------------------------------------------------*/
    public synchronized void loadRepositories(final String userLoginName, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        String authHeader = sharedPref.getString("Authorization", null);
        UserClient client = retrofit.create(UserClient.class);
        Call<List<Repository>> call = client.reposForUser(authHeader, userLoginName);
        //Log.d("CheckUserName", userLoginName);
        final String username = sharedPref.getString("userloginname", null);

        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                final List<Repository> repos = response.body();
                if (repos != null && !repos.isEmpty()) {
                    for (Repository rep : repos) {
                        rep.setUsername(userLoginName);
                        if(username.equals(userLoginName))
                        {
                            rep.setRepoType("userrepo");
                        }
                        else
                        {
                            rep.setRepoType("other");
                        }
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
                //Toast.makeText(FileList.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public synchronized LiveData<List<Repository>> getRepositories() {
        repositories = repositoryDB.repositoryDAO().getRepositories();
        return repositories;
    }

    public synchronized LiveData<List<Repository>> getRepositories(String repoType) {
        repositories = repositoryDB.repositoryDAO().getRepositories(repoType);
        return repositories;
    }
    /*-------------------------------------Repositories Info--------------------------------------------------*/

    /*-------------------------------------Files Info--------------------------------------------------*/
    public synchronized void loadFiles(String userLoginName, String repoName, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        String authHeader = sharedPref.getString("Authorization", null);
        UserClient client = retrofit.create(UserClient.class);
        Call<List<File>> call = client.filesOfRepo(authHeader, userLoginName, repoName);
        Log.d("CheckUserName", userLoginName);

        call.enqueue(new Callback<List<File>>() {
            @Override
            public void onResponse(Call<List<File>> call, Response<List<File>> response) {
                final List<File> repos = response.body();
                if (repos != null && !repos.isEmpty()) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            repositoryDB.fileDAO().insertFile(repos);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<File>> call, Throwable t) {
                Log.d("Error", "error :(");
                //Toast.makeText(FileList.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public synchronized LiveData<List<File>> getFiles() {
        /*executor.execute(new Runnable() {
            @Override
            public void run() {
                files = repositoryDB.fileDAO().getFiles();
            }
        });*/
        files = repositoryDB.fileDAO().getFiles();
        return files;
    }

    /*-------------------------------------Files Info--------------------------------------------------*/

    /*-------------------------------------Load Users Info--------------------------------------------------*/
    public List<User> loadUsers(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        String authHeader = sharedPref.getString("Authorization", null);
        UserClient client = retrofit.create(UserClient.class);
        Call<List<User>> call = client.loadUsers(authHeader);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                final List<User> users = response.body();
                if (users != null && !users.isEmpty()) {
                    //Log.d("checkloadedusernull", users.get(0).getLogin());
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            setLoadedUsers(users);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("checkloadedusernull", "error :( , "+t.getCause());
                //Toast.makeText(FileList.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
        if (loadedUsers!=null)
        {
            return loadedUsers;
        }
        else
        {
            return null;
        }
    }

    public synchronized void setLoadedUsers(List<User> loadedUsers) {
        this.loadedUsers = loadedUsers;
        /*if (loadedUsers!=null)
        {
            Log.d("checkloadedusernull", loadedUsers.get(0).getLogin());
            return true;
        }
        else
        {
            Log.d("checkloadedusernull", "nulllllll");
        }
        return false;*/
    }

    public synchronized List<User> getLoadedUsers() {
        return loadedUsers;
    }

    /*-------------------------------------Load Users Info--------------------------------------------------*/

    /*-------------------------------------User Info--------------------------------------------------*/
    public void loginUser(String username, String password, Context context) {
        AppRepository.this.username = username;
        AppRepository.this.password = password;
        AppRepository.this.context = context;

        new UserLoginTask().execute();
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public boolean getLoginStatus() {
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
    /*-------------------------------------User Info--------------------------------------------------*/
}
