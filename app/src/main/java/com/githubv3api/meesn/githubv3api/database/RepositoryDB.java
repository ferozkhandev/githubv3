package com.githubv3api.meesn.githubv3api.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Repository.class}, version = 1)
public abstract class RepositoryDB extends RoomDatabase {

    private static RepositoryDB instance;
    public abstract RepositoryDAO repositoryDAO();

    public static synchronized RepositoryDB getInstance(Context context) {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context, RepositoryDB.class, "Repositories.db").build();
        }
        return instance;
    }
}
