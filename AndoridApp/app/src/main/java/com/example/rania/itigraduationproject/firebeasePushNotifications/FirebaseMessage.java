package com.example.rania.itigraduationproject.firebeasePushNotifications;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.rania.itigraduationproject.HomeDriver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessage extends FirebaseMessagingService {
    NotificationManager notificationManager = (android.app.NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent=new Intent(this, HomeDriver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifactionBulider = new NotificationCompat.Builder(this)
                .setContentTitle("Information")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager.notify(1, notifactionBulider.build());

    }
}
