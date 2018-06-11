package com.example.rania.itigraduationproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.alarmPk.Alarm_receiver;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsOfEventForUser extends AppCompatActivity {

    int pending_id;
    Context context;
    AlarmManager alarmManage;
    PendingIntent pending_intent;
    PendingIntent pending_intent_send_Again;
    DBDriverConnection dbDriverConnection;
    private static Retrofit retrofit = null;
    Service service;
    Intent alarm_intent;



    protected void onStart() {
        super.onStart();
        if(!CheckInternetConnection.isNetworkAvailable(this))
        {
            CheckInternetConnection.bulidDuligo(this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_event_for_user);

        //objects
        this.context=this;
        alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent my_intent = this.getIntent();

        alarm_intent = new Intent(this.context,Alarm_receiver.class);

        final Intent alarm_intent_again = new Intent(this.context,Alarm_receiver.class);
        final Calendar calendar = Calendar.getInstance();

        pending_id = Integer.parseInt((String) my_intent.getExtras().get("id"));

        pending_intent=pending_intent.getBroadcast(DetailsOfEventForUser.this,pending_id
                ,alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
        dbDriverConnection = new DBDriverConnection(this);


        //resources
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        TextView userID = findViewById(R.id.userID);
//        userID.setText(pending_id);
        Toast.makeText(context, ""+pending_id, Toast.LENGTH_SHORT).show();



        //actions

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    //tb3n kol dh lazem yt8ayer ll dbconnection haaaaaaaaaaa

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
