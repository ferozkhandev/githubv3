package com.githubv3api.meesn.githubv3api.service;

import android.arch.lifecycle.LiveData;

import com.githubv3api.meesn.githubv3api.database.Repository;
import com.githubv3api.meesn.githubv3api.model.File;
import com.githubv3api.meesn.githubv3api.model.OtherUsers;
import com.githubv3api.meesn.githubv3api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserClient {
    @GET("user")
    Call<User> userlogin(@Header("Authorization") String encodedid);
    @GET("/users")
    Call<List<OtherUsers>> loadUsers(@Header("Authorization") String encodedid);
    @GET("/users/{user}/repos")
    Call<List<Repository>> reposForUser(@Header("Authorization") String encodedid, @Path("user") String user);
    @GET("/repos/{user}/{repo}/contents")
    Call<List<File>> filesOfRepo(@Header("Authorization") String encodedid, @Path("user") String user, @Path("repo") String repo);
}
