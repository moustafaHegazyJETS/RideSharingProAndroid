package com.example.rania.itigraduationproject.firebeasePushNotifications;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebeaseIncstanceIdDevice extends FirebaseInstanceIdService{
    public final static String REG_Token="REG_TOKEN";

    @Override
    public void onTokenRefresh()
    {
        String recentToken= FirebaseInstanceId.getInstance().getToken();
        Log.i("REG_Token",recentToken);
    }


}
