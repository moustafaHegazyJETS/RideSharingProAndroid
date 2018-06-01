package com.example.rania.itigraduationproject;

import android.content.Context;
import android.os.Bundle;
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

import com.example.rania.itigraduationproject.alarmPk.Alarm_receiver;

import java.util.Calendar;

public class DetailsOfEvent extends AppCompatActivity {

    TextView idFromIntent;

    Context context;

    AlarmManager alarmManage;
    PendingIntent pending_intent;
    PendingIntent pending_intent_send_Again;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        this.context=this;
        alarmManage = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent my_intent = this.getIntent();

        final Intent alarm_intent = new Intent(this.context,Alarm_receiver.class);

        final Intent alarm_intent_again = new Intent(this.context,Alarm_receiver.class);
        final Calendar calendar = Calendar.getInstance();

        final int pending_id = Integer.parseInt((String) my_intent.getExtras().get("id"));

        pending_intent=pending_intent.getBroadcast(DetailsOfEvent.this,pending_id
                ,alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);



        //resources
        idFromIntent = findViewById(R.id.idFromIntent);
        idFromIntent.setText(""+my_intent.getExtras().get("id"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



        //actions
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //act as aware of event
                alarm_intent.putExtra("Ex","off");

                alarmManage.cancel(pending_intent);

                sendBroadcast(alarm_intent);

                finish();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // hna han3ml alsho8l bta3 al cancel bardo
        Toast.makeText(context, "Please Press Button", Toast.LENGTH_SHORT).show();
    }

}
