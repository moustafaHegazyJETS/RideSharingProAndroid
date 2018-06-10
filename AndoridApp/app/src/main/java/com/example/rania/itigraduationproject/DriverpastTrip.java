package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DriverpastTrip extends AppCompatActivity {
    private static Retrofit retrofit = null;
    Service service;
    ListView pastTripList;
    User userobject;
    ArrayList<String> listNames;


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
        setContentView(R.layout.activity_past_trip_for_driver);

        //Objects
        //Retrofit -----------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
        userobject=(User)getIntent().getExtras().get("driverUser");
        final List<Trip> tripArray=new ArrayList<>();
        listNames=new ArrayList<>();

        //Resources
        pastTripList=(ListView)findViewById(R.id.pastTripToDriver);


        //Actions
        service.getAllpastTripstoDeriver(userobject).enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                Toast.makeText(DriverpastTrip.this, "Enter in method get all  trips", Toast.LENGTH_SHORT).show();

                if (response.body()!=null)
                {
                    if(response.body().size()>0)
                    {

                        for (int i =0 ; i<response.body().size() ; i++)
                        {
                            tripArray.add(i,response.body().get(i));
                            listNames.add(response.body().get(i).getTripName().toString());
                        }


                        ArrayAdapter<String> adapter =new ArrayAdapter<>(DriverpastTrip.this, android.R.layout.simple_list_item_1,listNames);
                        pastTripList.setAdapter(adapter);

                        pastTripList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent in = new Intent(DriverpastTrip.this, TripDetailsForDriver.class);
                                in.putExtra("tripVal", (Serializable) tripArray.get(i));
                                in.putExtra("driverUser",(Serializable)userobject);
//                                in.putExtra("tripVal",(Serializable) tripArray.get(i) );
//                                in.putExtra("driverUser",(Serializable) driverUser );
                                startActivity(in);
                            }});
                    }else {
                        Toast.makeText(DriverpastTrip.this, "No Past Trip up Till Now", Toast.LENGTH_SHORT).show();
                    }
                } else{

                    Toast.makeText(DriverpastTrip.this, "Response body  null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Toast.makeText(DriverpastTrip.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
