package com.example.rania.itigraduationproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import java.io.Serializable;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripSearch extends AppCompatActivity {

    EditText fromEdit;
    EditText toEdit;
    Button searchBtn;
    String startPoint = "";
    String destination = "";
    SessionManager session_mangement;
    Service service;
    double toLongtiude;
    double fromLatitude;
    double toLatitude;
    double fromLongtiude;
    private static Retrofit retrofit = null;
    public static final int PLACE_PICKER_REQUEST1 = 1;
    public static final int PLACE_PICKER_REQUEST2 = 2;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);

        //objects
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);


        //resources

        fromEdit = (EditText) findViewById(R.id.fromTxt);
        toEdit =(EditText) findViewById(R.id.toTxt);
        searchBtn = findViewById(R.id.search_button);


        //actions

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromEdit.getText().equals("") || toEdit.getText().equals("")) {
                    Toast.makeText(TripSearch.this, "Check Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Trip t = new Trip();
                    t.setFrom(fromEdit.getText().toString());
                    t.setTo(toEdit.getText().toString());
                    requestTrip(t);
//                         Toast.makeText(TripSearch.this, "Please Fill all", Toast.LENGTH_SHORT).show();


                }
            }
        });

        //------------------Action to To Edit Text---------
        fromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TripSearch.this, "from edit text Action", Toast.LENGTH_SHORT).show();
                action_From_To();
            }
        });

        //----------------------Action To FromEdit
        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TripSearch.this, "to edit text Action", Toast.LENGTH_SHORT).show();

                action_From_To();

            }
        });
         }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PLACE_PICKER_REQUEST1) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);

                    if (place.equals(null)) {

                        Toast.makeText(this, "place is null", Toast.LENGTH_LONG).show();
                    }

                    String toastMsg = String.format("Place that you need is : ", place.getAddress().toString());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    fromEdit.setText(place.getAddress().toString());
                    startPoint = place.getAddress().toString();
                    fromLatitude = place.getLatLng().latitude;
                    fromLongtiude = place.getLatLng().longitude;
                }
            }
            if (requestCode == PLACE_PICKER_REQUEST2) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);

                    if (place.equals(null)) {

                        Toast.makeText(this, "place is null", Toast.LENGTH_LONG).show();
                    }

                    String toastMsg = String.format("Place that you need is : ", place.getAddress().toString());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    destination = place.getAddress().toString();
                    toEdit.setText(destination);
                    toLatitude = place.getLatLng().latitude;
                    toLongtiude = place.getLatLng().longitude;
                }
            }

        }



    public void requestTrip(Trip t)
    {
        service.getTripsFromSearch(t).enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {

                if(response.body()==null )
                {
                    Toast.makeText(TripSearch.this,"respone is  "+response.body(),Toast.LENGTH_SHORT).show();
                }

                if (response.body()!=null)
                {//Here To Write Operation Code

//                    Toast.makeText(TripSearch.this, ""+response.body().get(0).getTripName(), Toast.LENGTH_SHORT).show();

//                    Toast.makeText(TripSearch.this, ""+response.body().get(0).getTripName(), Toast.LENGTH_SHORT).show();
                    Intent intent_home = new Intent(getApplicationContext(), TripShowActivity.class);
                    intent_home.putExtra("tripList", (Serializable) response.body() );
                    startActivity(intent_home);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Toast.makeText(TripSearch.this,"on Failere"+t,Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Alert Function
    public void showSettingsAlerts()
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled . Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

            }
        });
    }

    //Network Alert Setting
    public void showSettingsAlertsForNetwork() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network is settings");
        alertDialog.setMessage("Network is not enabled . Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(i);

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }

    //Function to Action when click  to ediText
    public void action_From_To()
    {

        if (ActivityCompat.checkSelfPermission(TripSearch.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(TripSearch.this, "You need to enable location first", Toast.LENGTH_SHORT).show();
            showSettingsAlerts();
            return;
        }
        if (ActivityCompat.checkSelfPermission(TripSearch.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(TripSearch.this, "You need to enable Network first", Toast.LENGTH_SHORT).show();
            showSettingsAlertsForNetwork();
            return;
        }


        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


            startActivityForResult(builder.build(TripSearch.this), PLACE_PICKER_REQUEST1);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
