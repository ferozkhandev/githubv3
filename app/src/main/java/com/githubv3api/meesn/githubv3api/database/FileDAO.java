package com.githubv3api.meesn.githubv3api.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.githubv3api.meesn.githubv3api.model.File;

import java.util.List;

@Dao
public interface FileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFile(File file);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFile(List<File> file);

    @Delete
    void deleteFile(File file);

    @Delete
    void deleteFile(List<File> file);

    @Update
    void updateFile(File file);

    @Update
    void updateFile(List<File> file);

    @Query("SELECT * FROM files")
    LiveData<List<File>> getFiles();

    @Query("SELECT * FROM files WHERE repoName=:repoName")
    LiveData<List<File>> getFiles(String repoName);
}
