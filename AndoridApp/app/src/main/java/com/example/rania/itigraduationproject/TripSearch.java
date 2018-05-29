package com.example.rania.itigraduationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripSearch extends AppCompatActivity {

    TextView fromTxt;
    TextView toTxt;
    Button searchBtn;

    private static Retrofit retrofit = null;
    SessionManager session_mangement;
    Service service;



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

        fromTxt = findViewById(R.id.fromTxt);
        toTxt = findViewById(R.id.toTxt);
        searchBtn = findViewById(R.id.search_button);



        //actions

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromTxt.getText().equals("") || toTxt.getText().equals(""))
                {
                    Toast.makeText(TripSearch.this, "Check Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Trip t = new Trip();

                    t.setFrom(fromTxt.getText().toString());
                    t.setTo(toTxt.getText().toString());


                    Toast.makeText(TripSearch.this, ""+fromTxt.getText().toString()+" "+toTxt.getText().toString(), Toast.LENGTH_SHORT).show();

                    requestTrip(t);


                }
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
                {//Here To Write Operation Code

//                    for (int i = 0 ; i < response.body().size();i++)
//                    {
//
//
//                    }

                    Toast.makeText(TripSearch.this, ""+response.body().get(0).getTripName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Toast.makeText(TripSearch.this,"on Failere"+t,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
