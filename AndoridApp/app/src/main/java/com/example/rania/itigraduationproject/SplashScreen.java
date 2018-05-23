package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.model.User;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         final SessionManager session;
         session = new SessionManager(getApplicationContext());
        int SPLASH_TIME_OUT = 3000;
        Thread th=new Thread(){



            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                try {
                    sleep(3000);

                    if(session.isLoggedIn())
                    {
                       Intent i = new Intent( SplashScreen.this, Login.class);
                       startActivity(i);
                       finish();


                    }
                    else {
                        session.checkLogin();
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




            }};
        th.start();




    }
}

