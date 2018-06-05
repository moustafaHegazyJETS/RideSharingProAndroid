package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewReservedUserDetails extends AppCompatActivity {

    TextView userName;
    TextView email;
    TextView phone ;

    Button delete;

    Intent openIntent;
    User user ;
    private static Retrofit retrofit = null;
    Service service;
    Trip trip;
    User driverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reserved_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // objects
        openIntent = getIntent();
        user = (User) openIntent.getExtras().get("user");
        trip = (Trip) openIntent.getExtras().get("trip");
        driverUser = (User) openIntent.getExtras().get("driverUser");


        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);


        //resources
        delete = findViewById(R.id.DeleteUser);
        userName = findViewById(R.id.UserName);
        userName.setText(user.getUserName());
        email = findViewById(R.id.Email);
        email.setText(user.getEmail());
        phone = findViewById(R.id.Phone);
        phone.setText(user.getMobile());


        // actions
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                List<Integer> tripAndUserId = new ArrayList<>();
                tripAndUserId.add(0,trip.getIdTrip());
                tripAndUserId.add(1,user.getIdUser());

                service.deleteReservation(tripAndUserId).enqueue(new Callback<Trip>() {
                    @Override
                    public void onResponse(Call<Trip> call, Response<Trip> response) {
                        if(response.body()!=null&&response.body().getTripName().equals("Done"))
                        {
                            Toast.makeText(ViewReservedUserDetails.this, "Done Removing User", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeDriver.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("user",driverUser);
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(ViewReservedUserDetails.this, "something error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Trip> call, Throwable t) {
                        Toast.makeText(ViewReservedUserDetails.this, "something Failed", Toast.LENGTH_SHORT).show();
                        System.out.println(t);

                    }
                });

            }
        });


    }

}
