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

    @Update
    void updateRepository(Repository repository);

    @Query("SELECT * FROM repositories")
    LiveData<List<Repository>> getRepositories();
}
