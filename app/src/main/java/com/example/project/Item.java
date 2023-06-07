package com.example.project;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@androidx.room.Entity()
public class Item {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "name")//this is type
    String name;

    @ColumnInfo(name = "picture")//this is price
    String picture;



    public Item(String type, String picture) {
        this.name = type;
        this.picture = picture;
    }

    public Item() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return name;
    }

    public String getDate() {
        return picture;
    }

}
