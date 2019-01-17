package com.githubv3api.meesn.githubv3api.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "otherusers")
public class OtherUsers {

    @PrimaryKey
    private int id;

    private String login;

    @Ignore
    public OtherUsers() {
    }

    @Ignore
    public OtherUsers(String login) {
        this.login = login;
    }

    public OtherUsers(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
