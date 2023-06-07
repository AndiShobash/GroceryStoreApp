package com.example.project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@SuppressWarnings("ALL")
@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);
    @Update
    void update(Item item);
    @Delete
    void delete(Item item);

    @Query("SELECT * FROM Item")
    List<Item> getAllItems();

//    @Insert
//    void insertAll(Item... items);
}
