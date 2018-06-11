package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {

    private static Retrofit retrofit = null;
    SessionManager session_mangement;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

                //---------------SharedPrefrences ----------
                session_mangement = new SessionManager(getApplicationContext());


        //Retrofit -----------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
        //------------------------------------------------

        if (session_mangement.isLoggedIn()) {
            HashMap<String, String> user_data = session_mangement.getUserDetails();
            User user = new User();
            user.setEmail(user_data.get(SessionManager.KEY_EMAIL));
            user.setPassword(user_data.get(SessionManager.KEY_password));
            Log.i("username", user_data.get(SessionManager.KEY_EMAIL));
            Log.i("password", user_data.get(SessionManager.KEY_password));
//            requestUser(user);
            service.getUserByEmailAndPassword(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, final Response<User> response) {

                    if (response.body() == null) {
                        Toast.makeText(SplashScreen.this, "Login Failed Due to response" + response.body(), Toast.LENGTH_SHORT).show();
                        Thread th = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(3000);
                                    Intent i = new Intent(SplashScreen.this, Login.class);
                                    startActivity(i);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        th.start();
                    }

                    if (response.body() != null) {
                        session_mangement.createLoginSession(response.body().getEmail(), response.body().getPassword());
                        Thread th = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(3000);
                                    if(response.body().getDriverCarInfo()!=null){
                                        System.out.println("_____________________________________");
                                        System.out.println(response.body().getDriverCarInfo().getCarModel());
                                        Intent driver_i = new Intent(SplashScreen.this, HomeDriver.class);
                                        driver_i.putExtra("user", response.body());
                                        startActivity(driver_i);
                                        finish();

                                    }else
                                    {
                                        Intent i = new Intent(SplashScreen.this, HomeUser.class);
//                                        System.out.println(response.body().getDriverCarInfo().getCarModel());
                                        i.putExtra("user", response.body());
                                        startActivity(i);
                                        finish();
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        th.start();


                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                   // Toast.makeText(SplashScreen.this, "on Failere", Toast.LENGTH_SHORT).show();
                    Thread th = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(3000);
                                Intent i = new Intent(SplashScreen.this, Login.class);
                                startActivity(i);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    th.start();

                }
            });

        } else {
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            th.start();
        }


    }
}

