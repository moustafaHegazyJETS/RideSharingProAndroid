package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

public class UserShowReservedTripDetails extends AppCompatActivity {
    EditText tripName;
    EditText tripTime;
    EditText tripFrom;
    EditText tripTo;
    EditText numOfSeatsTrip;
    EditText cost;
    EditText day;


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
        setContentView(R.layout.activity_user_show_reserved_trip_details);

        //objects
        Intent intent=getIntent();
        final Trip t=(Trip) intent.getSerializableExtra("trip");

        //Resources
        if(t!=null) {
            tripName = (EditText) findViewById(R.id.TripNameEdt);
            tripName.setText(t.getTripName());
            tripTime = (EditText) findViewById(R.id.TimeTripEdt);
            tripTime.setText(t.getTime());
            tripFrom = (EditText) findViewById(R.id.SourceTripEdt);
            tripFrom.setText(t.getFrom());
            tripTo = (EditText) findViewById(R.id.destinationTripEdt);
            tripTo.setText(t.getTo());
            numOfSeatsTrip = (EditText) findViewById(R.id.numOfSeatsEdt);
            numOfSeatsTrip.setText(t.getNumberOfSeats().toString());
            cost = (EditText) findViewById(R.id.costEDT);
            cost.setText("" + t.getCost());
            day = (EditText) findViewById(R.id.dayEdt);
            day.setText(t.getDay());
        }
    }
}
