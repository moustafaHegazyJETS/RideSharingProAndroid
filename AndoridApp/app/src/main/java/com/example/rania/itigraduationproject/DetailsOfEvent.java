package com.example.rania.itigraduationproject;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.alarmPk.Alarm_receiver;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsOfEvent extends AppCompatActivity {

    //Event Screen
    TextView idFromIntent;
    Context context;
    AlarmManager alarmManage;
    PendingIntent pending_intent;
    PendingIntent pending_intent_send_Again;
    DBDriverConnection dbDriverConnection;
    private static Retrofit retrofit = null;
    Service service;
    int pending_id;
    Intent alarm_intent;
    //work  to alert dialog
    Ringtone r;
    Button startBtn;
    Button cancelBtn;
    TextView tripNameTxtV;
    TextView fromTxtV;
    TextView toTxtV;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    RemoteViews remoteViews;
    Trip trip;


    protected void onStart() {
        super.onStart();
        if(!CheckInternetConnection.isNetworkAvailable(this))
        {
            CheckInternetConnection.bulidDuligo(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_details_of_event);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        //objects

        //--------------------------------------------------------------------work  6/10/2018
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        r = RingtoneManager.getRingtone(this, notification);
//        r.play();
//
//        Vibrator vibrator = (Vibrator) this
//                .getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(4000);

        //Objects ------------------------
        this.context=this;
        alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent my_intent = this.getIntent();

        alarm_intent = new Intent(this.context,Alarm_receiver.class);

        final Intent alarm_intent_again = new Intent(this.context,Alarm_receiver.class);

        final Calendar calendar = Calendar.getInstance();

        pending_id = Integer.parseInt((String) my_intent.getExtras().get("id"));

        pending_intent=pending_intent.getBroadcast(DetailsOfEvent.this,pending_id
                ,alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);

        dbDriverConnection = new DBDriverConnection(this);

        Dialog dialog = new Dialog(DetailsOfEvent.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_layout);
        //Resources -----------------------------------------
        startBtn = (Button) dialog.findViewById(R.id.startDialogtn);
        cancelBtn = (Button) dialog.findViewById(R.id.canceleDialogBtn);
        tripNameTxtV = (TextView) dialog.findViewById(R.id.dialogTripName);
        fromTxtV = (TextView) dialog.findViewById(R.id.dialogFrom);
        toTxtV = (TextView) dialog.findViewById(R.id.dialogTo);


//        //Request to  get Trip Object----------
//        service.getTripById(pending_id).enqueue(new Callback<Trip>() {
//            @Override
//            public void onResponse(Call<Trip> call, Response<Trip> response) {
//                if(response.body()!=null)
//                {
//                    fromTxtV.setText(response.body().getFrom());
//                    tripNameTxtV.setText(response.body().getTripName());
//                    toTxtV.setText(response.body().getTo());
//                    trip=new Trip();
//                    trip.setStartlongtiude(response.body().getStartlongtiude());
//                    trip.setStartlatitude(response.body().getStartlatitude());
//                    trip.setEndlongtiude(response.body().getEndlongtiude());
//                    trip.setEndlatitude(response.body().getEndlatitude());
//                    trip.setTripName(response.body().getTripName());
//
//
//                }
//                else
//                {
//                    Toast.makeText(context, "response body  is null ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Trip> call, Throwable t) {
//                Toast.makeText(context, "PleaseCheck  Internet connection", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        trip.setEndlatitude(34.9147921);
        trip.setEndlongtiude(-97.7786493);
        trip.setStartlatitude(30.044281);
        trip.setStartlongtiude(	31.340002);


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + trip.getStartlatitude() + "," + trip.getStartlongtiude()+ "&daddr=" +trip.getEndlatitude() + "," + trip.getEndlongtiude()));


        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        remoteViews.setTextViewText(R.id.notificationTripNameTxtV, "Your trip " +trip.getTripName());
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.notification_start, pendingIntent);








//Actions to  Button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update Local DB
                dbDriverConnection.setTripToBePast(pending_id);


                //Update WS DB
                //moshkela kbera lw ma3hosh net f alwa2t dh ?????????????????????????????????
                service.setTripToBePast( pending_id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                alarm_intent.putExtra("Ex","off");

                alarmManage.cancel(pending_intent);

                sendBroadcast(alarm_intent);

                finish();

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update Local DB
                dbDriverConnection.setTripToBePast(pending_id);


                //Update WS DB
                //moshkela kbera lw ma3hosh net f alwa2t dh ?????????????????????????????????
                service.setTripToBePast( pending_id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                alarm_intent.putExtra("Ex","off");

                alarmManage.cancel(pending_intent);

                sendBroadcast(alarm_intent);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + 30.0444 + "," + 31.2357 + "&daddr=" +31.2001 + "," + 29.9187));

                startActivity(intent);

                finish();
            }
        });


        //resources
       // idFromIntent = findViewById(R.id.idFromIntent);
        //idFromIntent.setText(""+pending_id);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



//        //actions
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                doTask(pending_id,alarm_intent);
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        // hna han3ml alsho8l bta3 al cancel bardo
        Toast.makeText(context, "Please Press Button", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        doTask(pending_id,alarm_intent);
    }

    public void doTask(int pending_id , Intent alarm_intent)
    {
        //act as aware of event
        //Update Local DB
        dbDriverConnection.setTripToBePast(pending_id);
        //Update WS DB
        //moshkela kbera lw ma3hosh net f alwa2t dh ?????????????????????????????????
        service.setTripToBePast( pending_id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });



        //--------------------------

        alarm_intent.putExtra("Ex","off");

        alarmManage.cancel(pending_intent);

        sendBroadcast(alarm_intent);

        finish();


    }

}
