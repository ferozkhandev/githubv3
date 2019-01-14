package com.githubv3api.meesn.githubv3api.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "repositories")
public class Repository {

    @PrimaryKey
    private int id;

    private String repoType;
    private String name;

    @Ignore
    public Repository() {
    }

    @Ignore
    public Repository(String repoType, String name) {
        this.repoType = repoType;
        this.name = name;
    }

    public Repository(int id, String repoType, String name) {
        this.id = id;
        this.repoType = repoType;
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public String getRepoType() {
        return repoType;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
