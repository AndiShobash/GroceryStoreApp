package com.example.project;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@androidx.room.Entity()
public class Order {
    @PrimaryKey(autoGenerate = true)
     int id;
    @ColumnInfo(name = "firstname")
     String firstname;
    @ColumnInfo(name = "lastname")
     String lastname;
    @ColumnInfo(name = "address")
     String address;
    @ColumnInfo(name = "type")
     String type;
    @ColumnInfo(name = "phonenumber")
     String phonenumber ;
    @ColumnInfo(name = "quantity")
     int quantity;
    @ColumnInfo(name = "totalprice")
     double  totalprice;
    @ColumnInfo(name = "date")
     String  date;
    @ColumnInfo(name = "sms")
    String  sms;
    @ColumnInfo(name = "saveorder")
    Boolean  saveorder;


    public Order(String firstname, String lastname, String address, String type, String phonenumber, int quantity, double totalprice, String date, String sms, Boolean saveorder) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.type = type;
        this.phonenumber = phonenumber;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.date = date;
        this.sms = sms;
        this.saveorder = saveorder;
    }

    public Boolean getSaveorder() {
        return saveorder;
    }

    public String getSms() {
        return sms;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }
}
