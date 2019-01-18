package com.githubv3api.meesn.githubv3api.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RepositoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRepository(Repository repository);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRepository(List<Repository> repository);

    @Delete
    void deleteRepository(Repository repository);

    @Delete
    void deleteRepository(List<Repository> repository);

    @Query("DELETE FROM repositories WHERE username=:username;")
    void deleteRepository(String username);

    @Update
    void updateRepository(Repository repository);

    @Query("SELECT id, username, repoType, name FROM repositories ORDER BY name ASC;")
    LiveData<List<Repository>> getRepositories();

    @Query("SELECT id, username, repoType, name FROM repositories WHERE repoType=:repoType ORDER BY name ASC;")
    LiveData<List<Repository>> getRepositories(String repoType);
}
