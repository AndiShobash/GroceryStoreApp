package com.example.project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/************************************************ Creating the orders DB  **********************************************************/

@Database(entities = {Order.class},version = 1)
public abstract class OrderDB extends RoomDatabase {
    private static OrderDB instance;

    public abstract OrderDao getOrderDao();

    public static OrderDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), OrderDB.class, "order_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
