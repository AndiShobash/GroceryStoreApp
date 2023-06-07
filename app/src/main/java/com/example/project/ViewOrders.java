package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewOrders extends OptionsMenu {

    private static OrderViewModel orderViewModel;

    /************************************************ creating the recyclerview for the orders  **********************************************************/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders);
        RecyclerView recyclerView=findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        orderViewModel=new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(OrderViewModel.class);
        OrderAdapter adapter=new OrderAdapter(ViewOrders.this,this,orderViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplication()));
    }
    /************************************************ creating the same menu as OptionsMenu the only difference is not opining another view order when it is clicked**********************************************************/

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                AlertDialog.Builder a_builder = new AlertDialog.Builder(ViewOrders.this);
                a_builder.setTitle("Exiting the application")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent serviceIntent = new Intent(ViewOrders.this.getApplicationContext(), Service.class);
                                stopService(serviceIntent);// Stops the Service before exiting
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                System.exit(1);                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                // alert.setTitle(R.string.alert_dialog_two_buttons_title);
                alert.show();
                return true;

            case R.id.Settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content ,new MainActivity.MyPreferences()).addToBackStack(null)
                        .commit();


                break;
            case R.id.Orders:
                Toast.makeText(ViewOrders.this, "in Orders", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

}
