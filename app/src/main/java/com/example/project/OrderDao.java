package com.example.project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao
{
    @Insert
    void insert(Order order);
    @Update
    void update(Order order);
    @Delete
    void delete(Order order);

    @Query("SELECT * FROM `Order`")
     List<Order> getAllOrder();
}
