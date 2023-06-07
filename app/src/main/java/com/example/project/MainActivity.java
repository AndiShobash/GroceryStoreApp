package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends OptionsMenu {
    private static ItemViewModel itemViewModel;
   // BroadcastReceiver br = new MyReceiver();
   // IntentFilter filter = new IntentFilter();;
    int[] images={R.drawable.apple,R.drawable.cabbage,R.drawable.grapes,R.drawable.cucumber,R.drawable.eggplant,R.drawable.orange,R.drawable.peach,R.drawable.watermelon,R.drawable.pumpkin,R.drawable.potato,R.drawable.tomato};
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /************************************************ Creating the RecycleView for the items **********************************************************/

        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        itemViewModel=new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        ItemAdapter adapter=new ItemAdapter(MainActivity.this,this,itemViewModel,images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplication()));

        /************************************************ Asking permission to send SMS **********************************************************/
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        /************************************************ Calling create Service function **********************************************************/

        callService();

    }


    /************************************************ Creating Service **********************************************************/
    private void callService() {
        if(!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this, Service.class);
            startForegroundService(serviceIntent);
        }
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(Service.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    /************************************************ Creating menu from the class Options Menu**********************************************************/

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    /************************************************ Going to the OrderItem activity and sending the price and the type of the item **********************************************************/

    public void call_app_order_item(Item item) {
        Intent intent = new Intent(MainActivity.this,OrderItem.class);
        intent.putExtra("price",item.picture);
        intent.putExtra("type",item.getType());
        startActivity(intent);
    }

    /************************************************ Creating SP **********************************************************/

    public static class MyPreferences extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.mypreferencescreen, rootKey);

        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            view.setBackgroundColor(Color.WHITE);
            super.onViewCreated(view, savedInstanceState);
        }
    }
}