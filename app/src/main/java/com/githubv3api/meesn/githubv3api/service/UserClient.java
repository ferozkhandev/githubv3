package com.githubv3api.meesn.githubv3api.service;

import com.githubv3api.meesn.githubv3api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserClient {
    @GET("user")
    Call<User> userlogin(@Header("Authorization") String encodedid);
    @GET("/users/{user}/repos")
    Call<List<User>> reposForUser(@Path("user") String user);
}
