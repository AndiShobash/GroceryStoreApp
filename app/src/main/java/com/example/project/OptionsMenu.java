package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsMenu extends AppCompatActivity {

    /************************************************ inflating the menu XML **********************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    /************************************************ giving each icon what is going to do if it is clicked **********************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit://if selected exit then it is exited from the app
                AlertDialog.Builder a_builder = new AlertDialog.Builder(OptionsMenu.this);
                a_builder.setTitle("Exiting the application")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //exiting from the app
                                Intent serviceIntent = new Intent(OptionsMenu.this.getApplicationContext(), Service.class);
                                stopService(serviceIntent);// Stops the Service before exiting
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                System.exit(1);

                            }
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

            case R.id.Settings:// if selected settings then it will show the sp options
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content ,new MainActivity.MyPreferences()).addToBackStack(null)
                        .commit();


                break;
            case R.id.Orders://if selected orders the it will go to see the orders in the db
                view_app_order_item();


        }


        return super.onOptionsItemSelected(item);
    }

    /************************************************ going to the ViewOrders to see the orders in the DB **********************************************************/

    public void view_app_order_item() {
        Intent intent = new Intent(OptionsMenu.this,ViewOrders.class);
        startActivity(intent);
    }
}
