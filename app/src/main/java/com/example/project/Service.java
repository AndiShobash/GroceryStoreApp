package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.security.Provider;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Service extends android.app.Service {

    BroadcastReceiver br = new MyReceiver();
    IntentFilter filter = new IntentFilter();;
    private boolean send_reminder=true;
    private List<Order> OrderList= new ArrayList<>();
    private Order current_order;
    private LocalDate date;
    OrderDB db;

    /************************************************ Broadcast to check if the date is changed is so then it tells the service about it  **********************************************************/

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
                Toast.makeText(context, "Date Changed", Toast.LENGTH_SHORT).show();
                send_reminder=true;
            }
        }
    }

    /************************************************ Send SMS if the order is due tomorrow  **********************************************************/

    private void sendSMS(String phoneNumber, String type,String firstname,String address){
        String msg="Hello "+firstname+", You're order of "+type+" is going to arrive in this address: "+address;
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,msg,null,null);
        //+1-555-521-5554
    }

    /************************************************ creates the Broadcast **********************************************************/

    @Override
    public void onCreate(){
        super.onCreate();
        filter.addAction("android.intent.action.DATE_CHANGED");
        registerReceiver(br, filter);
    }

    /************************************************ always checking with the orders if the date is due tomorrow or not if so its send SMS**********************************************************/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            if(send_reminder==true) {
                                date= LocalDate.now();
                                date=date.plusDays(1);
                                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                String formattedDate = date.format(myFormatObj);
                                db = OrderDB.getInstance(Service.this);
                                OrderList = db.getOrderDao().getAllOrder();
                                for (int i=0;i< OrderList.size();i++)
                                {
                                    current_order=OrderList.get(i);
//                                    current_order.sms="Yes";
//                                    db.getOrderDao().update(current_order);
                                    if(current_order.getDate().equals(formattedDate) && current_order.getSms().equals("Yes")){
                                        sendSMS(current_order.getPhonenumber(),current_order.getType(),current_order.getFirstname(),current_order.getAddress());
//                                        current_order.sms="No";
//                                        db.getOrderDao().update(current_order);
                                    }
                                }
                                send_reminder=false;
                                sendBroadcast(intent);
                            }
                            Log.d("Service","Service is running");
                            try{
                                Thread.sleep(2000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        /************************************************ creating the service and showing a small icon in the notifications in the phone that the service is running**********************************************************/

        final String CHANNELID="Foreground Service ID";
        NotificationChannel channel=new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification=new Notification.Builder(this,CHANNELID)
                .setContentText("Service is running")
                .setContentTitle("Service enabled")
                .setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1001,notification.build());
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
