package com.githubv3api.meesn.githubv3api.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.githubv3api.meesn.githubv3api.model.OtherUsers;

import java.util.List;

@Dao
public interface OtherUsersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOtherUsersDAO(OtherUsers otherUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOtherUsersDAO(List<OtherUsers> otherUsers);

    @Update
    void updateOtherUsersDAO(OtherUsers otherUsers);

    @Update
    void updateOtherUsersDAO(List<OtherUsers> otherUsers);

    @Delete
    void deleteOtherUsersDAO(OtherUsers otherUsers);

    @Delete
    void deleteOtherUsersDAO(List<OtherUsers> otherUsers);

    @Query("SELECT * FROM otherusers")
    LiveData<List<OtherUsers>> getOtherUsers();
}
