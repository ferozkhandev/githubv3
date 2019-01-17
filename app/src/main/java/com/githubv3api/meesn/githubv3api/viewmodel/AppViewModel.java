package com.githubv3api.meesn.githubv3api.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.githubv3api.meesn.githubv3api.apprepository.AppRepository;
import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.model.File;
import com.githubv3api.meesn.githubv3api.model.User;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Repository>> repositories;
    private LiveData<List<File>> files;
    private boolean loginSuccess;
    private String userLoginName;
    private List<User> userList;

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void addRepository(Repository repository) {
        appRepository.addRepository(repository);
    }

    public void addRepository(List<Repository> repository) {
        appRepository.addRepository(repository);
    }

    public LiveData<List<Repository>> getRepositories() {
        repositories = appRepository.getRepositories();
        return repositories;
    }

    public LiveData<List<Repository>> getRepositories(String repoType) {
        repositories = appRepository.getRepositories(repoType);
        return repositories;
    }

    public void loginUser(String username, String password, Context context) {
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

    public void loadRepositories(String userLoginName, Context context) {
        appRepository.loadRepositories(userLoginName, context);
    }

    public void loadFiles(String userLoginName, String repoName, Context context) {
        appRepository.loadFiles(userLoginName, repoName, context);
    }

    public LiveData<List<File>> getFiles() {
        files = appRepository.getFiles();
        return files;
    }
    public LiveData<List<File>> getFiles(String repoName) {
        files = appRepository.getFiles(repoName);
        return files;
    }

    public List<User> loadUsers(Context context) {
        return appRepository.loadUsers(context);
    }

    public List<User> getLoadedUsers() {
        userList =  appRepository.getLoadedUsers();
        return userList;
    }
}
