package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripDetailsActivity extends AppCompatActivity {
    //Declaration
    EditText tripName;
    EditText tripTime;
    EditText tripFrom;
    EditText tripTo;
    EditText numOfSeatsTrip;
    Button joinTripBtn;
    Button driverDteailsBtn;
    private static Retrofit retrofit = null;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        //Objects ----------------
        Intent intent=getIntent();
        final Trip t=(Trip) intent.getSerializableExtra("trip");
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);



        //Resourses
        tripName=(EditText)findViewById(R.id.TripNameEdt);
        tripName.setText(t.getTripName());
        tripTime=(EditText)findViewById(R.id.TimeTripEdt);
        tripTime.setText(t.getTime());
        tripFrom=(EditText)findViewById(R.id.SourceTripEdt);
        tripFrom.setText(t.getFrom());
        tripTo=(EditText)findViewById(R.id.destinationTripEdt);
        tripTo.setText(t.getTo());
        numOfSeatsTrip=(EditText)findViewById(R.id.numOfSeatsEdt);
        numOfSeatsTrip.setText(t.getNumberOfSeats().toString());
        joinTripBtn=(Button)findViewById(R.id.JoinTrip);
        driverDteailsBtn=(Button)findViewById(R.id.driverDetails);

        //Actions

        driverDteailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDriverInfo(t);
            }
        });



    }


    public void requestDriverInfo(Trip trip) {
        service.getDriverInfo(trip).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Toast.makeText(TripDetailsActivity.this, ""+response.body().getUserName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }



}
