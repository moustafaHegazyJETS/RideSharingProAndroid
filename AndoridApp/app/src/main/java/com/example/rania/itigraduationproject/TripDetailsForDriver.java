package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.SqliteDBTrip.DBDriverConnection;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripDetailsForDriver extends AppCompatActivity {
    Intent openIntent ;
    Trip trip;
    TextView nameTxt;
    TextView fromTxt;
    TextView toTxt;
    TextView timeTxt;
    TextView dateTxt;
    Button viewReserved;
    User driverUser;
    DBDriverConnection dbDriverConnection;
    Trip newTrip ;
    private static Retrofit retrofit = null;
    Service service;

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
        setContentView(R.layout.activity_trip_details_for_driver);

        //objects
        openIntent = getIntent();
        trip = (Trip) openIntent.getExtras().get("tripVal");
       driverUser = (User) openIntent.getExtras().get("driverUser");
        Log.i("userid",driverUser.getUserName()) ;

        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);



        //resources
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameTxt = findViewById(R.id.tripNamet);
        nameTxt.setText("Trip Name is : "+trip.getIdTrip());
        fromTxt = findViewById(R.id.tripFromt);
        toTxt=findViewById(R.id.tripTot);
        dateTxt=findViewById(R.id.tripdatet);
        timeTxt = findViewById(R.id.tripTimet);
        viewReserved = findViewById(R.id.viewAllReserved);


        //actions

        //adding values to text fields
        getTripVal(trip);

        viewReserved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllReservedUsers(newTrip);

            }
        });



    }



    private void getTripVal(Trip trip) {
        service.getTrip(trip).enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if(response.body()!=null)
                {
                    newTrip = response.body();

                    nameTxt.setText(newTrip.getTripName());
                    fromTxt.setText(newTrip.getFrom());
                    toTxt.setText(newTrip.getTo());
                    timeTxt.setText(newTrip.getTime());
                    dateTxt.setText(newTrip.getDay());

                }
                else
                    Toast.makeText(TripDetailsForDriver.this, "Request Null", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Toast.makeText(TripDetailsForDriver.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                System.out.println(t);


            }
        });
    }

     int getAllReservedUsers(Trip trip)
    {
        final int[] numberOfReserves = new int[5];
        service.getReservedUsers(trip).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                numberOfReserves[0] =response.body().size();
                if(response.body()!=null)
                {
                    if(response.body().size()> 0)
                    {
                        Intent in =new Intent(TripDetailsForDriver.this,ViewReservedUsers.class);
                        in.putExtra("usersList",(Serializable) response.body());
                        in.putExtra("trip",(Serializable) newTrip);
                        in.putExtra("driverUser",(Serializable) driverUser);

                        startActivity(in);
                    }else
                    {
                        Toast.makeText(TripDetailsForDriver.this, "No User Reserved Yet", Toast.LENGTH_SHORT).show();
                    }
//
                }else
                {
                    Toast.makeText(TripDetailsForDriver.this, "Something Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(TripDetailsForDriver.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                System.out.println(t);
            }
        });
    return numberOfReserves[0];
    }



}
