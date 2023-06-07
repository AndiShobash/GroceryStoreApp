package com.example.project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/************************************************ In this class we order an item  **********************************************************/

public class OrderItem extends OptionsMenu implements SeekBar.OnSeekBarChangeListener  {

    public static  final String SHARED_PREFS="sharedPrefs";
    public final String SAVE_SMS="sms";
    private TextView total_price;
    private TextView show_quantity;
    private EditText etn_first_name;
    private EditText etn_lastname;
    private EditText etn_address;
    private EditText etn_phone_number;
    private RadioButton send_sms;
    private RadioButton send_sms_yes;
    private RadioButton send_sms_no;
    private RadioGroup radiogroup;
    private SeekBar seek_bar;
    private String value;
    private double price;
    private String type;
    private int i_counter;
    private Button checkout;
    private boolean i_counter_not_0;
    private boolean text_empty;
    private LocalDate date;
    private double checkout_price;
    OrderDB db;
    private List<Order> OrderList= new ArrayList<>();
    SharedPreferences app_preferences;
    private boolean save_order=false;
    private boolean send_confirmation=false;


protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.order_item);
    db=OrderDB.getInstance(OrderItem.this);
    total_price=(TextView) findViewById(R.id.total_sum);
    show_quantity=(TextView) findViewById(R.id.example);
    etn_first_name=(EditText) findViewById(R.id.first_name);
    etn_lastname=(EditText) findViewById(R.id.last_name);
    etn_address=(EditText) findViewById(R.id.address);
    etn_phone_number=(EditText) findViewById(R.id.phone_number);
    radiogroup=(RadioGroup) findViewById((R.id.radio_group));
    seek_bar =(SeekBar) findViewById((R.id.seekBar));
    seek_bar.setOnSeekBarChangeListener(this);
    checkout=(Button) findViewById(R.id.checkout);
    etn_first_name.addTextChangedListener(new TextListner());
    etn_lastname.addTextChangedListener(new TextListner());
    etn_address.addTextChangedListener(new TextListner());
    etn_phone_number.addTextChangedListener(new TextListner());
    show_quantity.addTextChangedListener(new TextListner());


    /************************************************ If clicked checkout then it will start to enter the order in place **********************************************************/

    checkout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /************************************************ Creating a dialog to ask if they are sure to buy the item **********************************************************/

            AlertDialog.Builder a_builder = new AlertDialog.Builder(OrderItem.this);
            a_builder.setTitle("Proceed to purchase?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        /************************************************ Selected yes **********************************************************/

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String first_name=etn_first_name.getText().toString().trim();
                            String last_name=etn_lastname.getText().toString().trim();
                            String address=etn_address.getText().toString().trim();
                            String phone_number=etn_phone_number.getText().toString().trim();
                            int select_id=radiogroup.getCheckedRadioButtonId();
                            if(select_id!=-1) {
                                send_sms = findViewById(select_id);
                                send_sms.getText().toString().trim();
                            }
                            String sms_string=send_sms.getText().toString().trim();
                            date= LocalDate.now();
                            date=date.plusDays(2);
                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            String formattedDate = date.format(myFormatObj);
                            checkout_price=Double.parseDouble(new DecimalFormat("##.##").format(checkout_price));
                            /************************************************ Checks in the SP if it is on the save order option if yes then its saves in the DB  **********************************************************/
                            app_preferences = PreferenceManager.getDefaultSharedPreferences(OrderItem.this);
                            save_order = app_preferences.getBoolean("save_order",false);
                            if(save_order==true) {
                                db.getOrderDao().insert(new Order(first_name, last_name, address, type, phone_number, i_counter, checkout_price, formattedDate, sms_string, save_order));
                            }
                            /************************************************ If the SP save sms is on and they selected send SMS as yes then send an SMS for the confirmed order  **********************************************************/

                            send_confirmation = app_preferences.getBoolean("checkBox",false);
                            if(send_confirmation==true){
                                if(sms_string.equals("Yes")) {
                                    sendSMS(phone_number, first_name);
                                }
                            }
                            OrderList=db.getOrderDao().getAllOrder();
                            /************************************************ After Finishing the order it will go back to the main activity**********************************************************/

                            Intent intent= new Intent(OrderItem.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        /************************************************ selected no **********************************************************/

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.show();

        }
    });

    /************************************************ gets from the main activity the price and the type of the item selected **********************************************************/

    Bundle extras = getIntent().getExtras();// Using the bundle we can get the sent items we want
    if (extras != null) {

        value = extras.getString("price");
        price= Double.parseDouble(value);
        type=extras.getString("type");
        //The key argument here must match that used in the other activity
    }


}

    /************************************************ Sends the confirmed order **********************************************************/

     private void sendSMS(String phoneNumber, String firstname){
        String msg="Hello "+firstname+", You're order has been confirmed.Thank you for shopping with us.";
         SmsManager smsManager=SmsManager.getDefault();
         smsManager.sendTextMessage(phoneNumber,null,msg,null,null);
         //+1-555-521-5554
     }




    private void saveSMS() {
        int select_id=radiogroup.getCheckedRadioButtonId();
             if(select_id!=-1) {
                 send_sms = findViewById(select_id);
                 send_sms.getText().toString().trim();
             }

    }


    /************************************************ checks if the fields are not empty  **********************************************************/

    private class TextListner implements  TextWatcher{

         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             String first_name=etn_first_name.getText().toString().trim();
             String last_name=etn_lastname.getText().toString().trim();
             String address=etn_address.getText().toString().trim();
             String phone_number=etn_phone_number.getText().toString().trim();

             if(first_name.length()==0||last_name.length()==0||address.length()==0|| phone_number.toString().length()==0) {

                     text_empty = false;
             }
             else{
                     text_empty = true;

             }
             if(text_empty==false) {
                 if(i_counter!=0){
                     checkout.setEnabled(false);
                 }

             }
             else{
                 if(i_counter!=0) {
                         checkout.setEnabled(true);

                 }
             }
         }

         @Override
         public void afterTextChanged(Editable editable) {

         }
     }

    /************************************************ updates the quntity in the seekbar and checks if it is on 0 (if yes the the btn checkout will be disabled) **********************************************************/

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        show_quantity.setText(" "+i);
        checkout_price=i*price;
        total_price.setText(String.format("%.2f",checkout_price));
        i_counter=i;
        if(i>0){
            i_counter_not_0=true;
        }
        else{
            i_counter_not_0=false;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
         if(text_empty==true&&i_counter>0){
             checkout.setEnabled(true);
         }
         else{
             checkout.setEnabled(false);
         }
    }

    /************************************************ creates the menu from the class OptionsMenu **********************************************************/

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
