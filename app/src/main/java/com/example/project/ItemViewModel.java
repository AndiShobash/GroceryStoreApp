package com.example.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/************************************************ viewModel for items **********************************************************/

public class ItemViewModel extends AndroidViewModel {
    ItemDB db=ItemDB.getInstance(this.getApplication().getApplicationContext());
     MutableLiveData<List<Item>> ItemLiveData;
     MutableLiveData<Integer> SelectedItem;
    public int SelectedItemPos;
    public static final String filename ="PrePopulateFile.txt";
    private int flag=0;
    Application application;

    public Context context;
    private static MainActivity main;

    //not making duplicates for the items in the store we use the file system in order for the app to remember
    //that this is not the first time the app is running
    public ItemViewModel(@NonNull Application application) {
        super(application);
        context=application.getApplicationContext();
        this.application=application;
        //read_file();
        readBySP();
        if(flag==0){
            //save_file();
            saveinsp();
            //read_file();
            readBySP();
            db.getItemDao().insert(new Item("Apple", "12.90"));
            db.getItemDao().insert(new Item("Cabbage", "8.90"));
            db.getItemDao().insert(new Item("Grapes", "7.90"));
            db.getItemDao().insert(new Item("Cucumber", "5.90"));
            db.getItemDao().insert(new Item("Eggplant", "10.90"));
            db.getItemDao().insert(new Item("Orange", "4.90"));
            db.getItemDao().insert(new Item("Peach", "9.90"));
            db.getItemDao().insert(new Item("Watermelon", "19.90"));
            db.getItemDao().insert(new Item("Pumpkin", "17.90"));
            db.getItemDao().insert(new Item("Potato", "4.90"));
            db.getItemDao().insert(new Item("Tomato", "6.90"));
        }
        if(SelectedItem==null) {
            SelectedItem=new MutableLiveData<>();
            SelectedItemPos = RecyclerView.NO_POSITION;
            SelectedItem.setValue(SelectedItemPos);
        }
    }
    /************************************************ reading from file  **********************************************************/

// This function reads from the file
    public void read_file() {
        FileInputStream fis =null;

        try {
            fis = getApplication().getApplicationContext().openFileInput(filename);
            if(fis !=null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                save_file();
                String text;
                text = br.readLine();
                flag=Integer.parseInt(text);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /************************************************ saving in file **********************************************************/

// This function saves to the file
    private void save_file() {
        try{

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplication().getApplicationContext().openFileOutput(filename, getApplication().getApplicationContext().MODE_PRIVATE));
                outputStreamWriter.write("1");
                outputStreamWriter.write("\n");

            outputStreamWriter.flush();
            outputStreamWriter.close();

        } catch (IOException e) {
        }

    }
    /************************************************ reading from SP **********************************************************/

    public void readBySP() {
        SharedPreferences sharedPref=application.getApplicationContext().getSharedPreferences("preference_key",Context.MODE_PRIVATE);
        String str = sharedPref.getString("notfirsttime","");
        if(!str.equals("")) {
            flag = Integer.parseInt(str);
        }
    }

    /************************************************ saving in SP **********************************************************/

    public void saveinsp()
    {

        SharedPreferences sharedPref=application.getApplicationContext().getSharedPreferences("preference_key",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        StringBuilder newString = new StringBuilder();
        newString.append("1");
        editor.putString("notfirsttime",newString.toString());

        editor.apply();
    }


    public MutableLiveData<Integer> getSelectedItemMutableLiveData() {
    return SelectedItem;
}
    public MutableLiveData<List<Item>> getItemsLiveData() {
        return ItemLiveData;
    }

    public void setSelectedItem(Integer Item){
        SelectedItemPos=Item;
        SelectedItem.setValue(SelectedItemPos);
    }
}
