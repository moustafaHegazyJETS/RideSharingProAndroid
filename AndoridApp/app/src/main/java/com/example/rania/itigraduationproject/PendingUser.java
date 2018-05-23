package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

public class PendingUser extends AppCompatActivity {
    TextView message_pendinguser;
    @Override
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
        setContentView(R.layout.activity_pending_user);
        Intent intent=getIntent();
        User user=(User) intent.getSerializableExtra("user");
        message_pendinguser=(TextView)findViewById(R.id.message_pending_user);
        message_pendinguser.setText("Hello"+""+user.getUserName()+"welcome to our application please wait while Admin Accept");
    }
}
