package com.githubv3api.meesn.githubv3api.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.githubv3api.meesn.githubv3api.apprepository.AppRepository;
import com.githubv3api.meesn.githubv3api.database.Repository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Repository>> repositories;
    private boolean loginSuccess;
    private String userLoginName;

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
        repositories = appRepository.getRepositories();
    }

    public void addRepository(Repository repository)
    {
        appRepository.addRepository(repository);
    }

    public void addRepository(List<Repository> repository)
    {
        appRepository.addRepository(repository);
    }

    public LiveData<List<Repository>> getRepositories()
    {
        repositories = appRepository.getRepositories();
        return repositories;
    }

    public void loginUser(String username, String password, Context context)
    {
        appRepository.loginUser(username, password, context);
    }

    public String getUserLoginName() {
        userLoginName = appRepository.getUserLoginName();
        return userLoginName;
    }

    public boolean getLoginStatus() {
        loginSuccess = appRepository.getLoginStatus();
        return loginSuccess;
    }

    public void loadRepositories(String userLoginName)
    {
        appRepository.loadRepositories(userLoginName);
    }
}
