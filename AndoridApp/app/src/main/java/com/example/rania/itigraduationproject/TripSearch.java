package com.example.rania.itigraduationproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripSearch extends AppCompatActivity {
    PlaceAutocompleteFragment locationcompleteFragment;
    PlaceAutocompleteFragment destinationcompleteFragment;
    ImageView destination_map;
    ImageView location_map;
    Button searchBtn;
    String startPoint = "";
    String destination = "";
    Service service;
    double toLongtiude;
    double fromLatitude;
    double toLatitude;
    double fromLongtiude;
    private static Retrofit retrofit = null;


    protected void onStart() {
        super.onStart();
        if (!CheckInternetConnection.isNetworkAvailable(this)) {
            CheckInternetConnection.bulidDuligo(this);
        }
    }


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


        searchBtn = findViewById(R.id.search_button);


        //actions

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (startPoint.equals("") || destination.equals("")) {
//                    Toast.makeText(TripSearch.this, "Check Fields", Toast.LENGTH_SHORT).show();
//                } else {
                  Trip t = new Trip();
                    ;
                    t.setFrom("a");
                    t.setTo("a");
                    requestTrip(t);
                //}
            }
        });

        //------------------Action to To Edit Text---------
        String locale = getResources().getConfiguration().locale.getCountry();
        locationcompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.location_autocomplete_fragment);
        destinationcompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry(locale)
                .build();
        destinationcompleteFragment.setFilter(autocompleteFilter);
        locationcompleteFragment.setFilter(autocompleteFilter);
        locationcompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                startPoint = place.getName().toString();
                fromLatitude = place.getLatLng().latitude;
                fromLongtiude = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place", status.getStatusMessage());
            }
        });

        destinationcompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                destination = place.getName().toString();
                toLongtiude = place.getLatLng().longitude;
                toLatitude = place.getLatLng().latitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place", status.getStatusMessage());
            }
        });
        location_map = findViewById(R.id.location_map);
        destination_map = findViewById(R.id.destination_map);

        location_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromMap(1);
            }
        });
        destination_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromMap(2);
            }
        });
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
                {
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

    void getLocationFromMap(int request_case){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), request_case);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:{
                if(resultCode == RESULT_OK){
                    Place place = PlacePicker.getPlace(this,data);
                    startPoint = place.getName().toString();
                    fromLatitude = place.getLatLng().latitude;
                    fromLongtiude = place.getLatLng().longitude;
                    locationcompleteFragment.setText(startPoint);
                }

            }
            case 2:{
                if(resultCode == RESULT_OK){
                    Place place = PlacePicker.getPlace(this,data);
                    destination = place.getName().toString();
                    toLongtiude = place.getLatLng().longitude;
                    toLatitude = place.getLatLng().latitude;
                    destinationcompleteFragment.setText(destination);
                }

            }
        }
    }
}
