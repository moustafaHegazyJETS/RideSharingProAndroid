package com.example.rania.itigraduationproject.alarmPk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.rania.itigraduationproject.Controllers.SessionManager;

public class Alarm_receiver extends BroadcastReceiver  {
    boolean flag=false;
    SessionManager session_mangement;
    @Override
    public void onReceive(Context context, Intent intent) {

//        session_mangement = new SessionManager(context);
        Log.i("receiver", "onReceive: sssssssssssssssssssssss ");

        String key = intent.getExtras().getString("Ex");

        Intent service_intent = new Intent(context,RingtonePlayingService.class);

        //pass the key
        service_intent.putExtra("Ex",key);
        service_intent.putExtra("id",intent.getExtras().getString("id"));
        service_intent.putExtra("who",intent.getExtras().getString("who"));

        Log.i(key, "onReceive: ");
//        if(session_mangement.isLoggedIn())
//        {
            if(key.equals("on")&&!flag)
            {
                context.startService(service_intent);
                flag=true;

            }
            else
            if(key.equals("off"))
            {
                context.startService(service_intent);
                flag=false;
            }
//        }else
//        {
//
//            //hna almfrod a5leeh kman y3ml ll trip cancel y3ny y5leha f al past
//        }




    }
}
