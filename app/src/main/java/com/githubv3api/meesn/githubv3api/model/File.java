package com.githubv3api.meesn.githubv3api.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "files")
public class File {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String download_url;
    private String type;
    private float size;

    @Ignore
    public File() {
    }

    @Ignore
    public File(String name, String download_url, String type, float size) {
        this.name = name;
        this.download_url = download_url;
        this.type = type;
        this.size = size;
    }

    public File(int id, String name, String download_url, String type, float size) {
        this.id = id;
        this.name = name;
        this.download_url = download_url;
        this.type = type;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
