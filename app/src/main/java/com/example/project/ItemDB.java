package com.example.project;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.security.PublicKey;
import java.util.List;
/************************************************ Creates the DB **********************************************************/

@Database(entities = {Item.class},version = 1)
public abstract class ItemDB extends RoomDatabase {

    private static ItemDB instance;

    public abstract ItemDao getItemDao();

    public static ItemDB getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), ItemDB.class,"item_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }



}


