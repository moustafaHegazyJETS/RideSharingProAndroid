package com.example.rania.itigraduationproject.alarmPk;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.rania.itigraduationproject.DetailsOfEvent;
import com.example.rania.itigraduationproject.DetailsOfEventForUser;
import com.example.rania.itigraduationproject.R;

public  class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    int start_id;
    boolean isRunning=false;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    int notification_id;
    RemoteViews remoteViews;
    Context context;

    Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ssaassaxas", "onStartCommand: ");



        String state = intent.getExtras().getString("Ex");
        String id =intent.getExtras().getString("id");
        String who = intent.getExtras().getString("who");

        assert state !=null;
        switch (state) {
            case "on":
                start_id=1;
                break;
            case "off":
                start_id=0;
                break;
            default:
                start_id=0;
                break;
        }




        if(!this.isRunning && start_id==1)
        {
            vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(3000);

            Log.i("kda awel mara ysht8al", "onStartCommand: ");
            mediaPlayer =MediaPlayer.create(this, R.raw.moustafaring);
            mediaPlayer.start();

            this.isRunning=true;
            this.start_id=0;

            Intent intent_notify = new Intent() ;


            if(who.equals("driver")){

                intent_notify = new Intent(this.getApplicationContext(),DetailsOfEvent.class);

            }else if(who.equals("user"))
            {
                 intent_notify = new Intent(this.getApplicationContext(),DetailsOfEventForUser.class);

            }
            intent_notify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent_notify.putExtra("id",id);

            this.getApplicationContext().startActivity(intent_notify);

//            PendingIntent pendingIntent =  PendingIntent.getActivity(this,Integer.parseInt(id),intent_notify,0);


            /////////////////////////////////////////////////////
//            //notifications
//
//            NotificationManager notificationManager = (NotificationManager)
//                    getSystemService(NOTIFICATION_SERVICE);
//
//            //the intent to go to main activirty
//            Intent intent_notify = new Intent(this.getApplicationContext(),MainActivity.class);
//
//
//
//            PendingIntent pendingIntent =  PendingIntent.getActivity(this,0,intent_notify,0);
//
//            Notification notification = new Notification.Builder(this)
//                    .setContentTitle("Please check the alarm ")
//                    .setContentText("Open activity ")
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.depositphotos)
//                    .setAutoCancel(true)
//                    .build();
//
//            notification.flags=Notification.FLAG_AUTO_CANCEL;
//
//            // set notification to notification manager
//            notificationManager.notify(0,notification);

/////////////////////////////////////////////////////////////////
            //custom notification
//            context=this;
//
//            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
//
//            remoteViews.setTextViewText(R.id.textView,"Welcome");
//            //remoteViews.setProgressBar(R.id.progressBar,100,20,true);
//
//            //Intent stop = new Intent();
//            Intent intent_notify = new Intent(this.getApplicationContext(),DetailsOfEvent.class);
////            int not_id = (int) System.currentTimeMillis();
//
//            Log.i("al id almrady", "ssssssssssssssssssssss "+id);
//
//            intent_notify.putExtra("id",id);
//
//            PendingIntent pendingIntent =  PendingIntent.getActivity(this,Integer.parseInt(id),intent_notify,0);
//
//            remoteViews.setOnClickPendingIntent(R.id.button,pendingIntent);
//
//            builder=new NotificationCompat.Builder(context);
//            builder.setSmallIcon(R.drawable.depositphotos)
//            .setAutoCancel(true).
//                    setCustomContentView(remoteViews).setContentIntent(pendingIntent)
//            ;
//
//            notificationManager.notify(Integer.parseInt(id),builder.build());


        }
        else if(this.isRunning && start_id==0)
        {
            Log.i("kda almfrood yo2af", "onStartCommand: ");
            vibrator.cancel();
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning=false;
            this.start_id=0;


        }else if(!this.isRunning && start_id==0)
        {
            this.isRunning=false;
            this.start_id=0;


        }
        else if(!this.isRunning && start_id==1)
        {
            this.isRunning=true;
            this.start_id=0;

        }
        else
        {

        }








        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        this.isRunning=false;
    }

}
