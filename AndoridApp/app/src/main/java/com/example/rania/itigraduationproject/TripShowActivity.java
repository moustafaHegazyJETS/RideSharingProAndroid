package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rania.itigraduationproject.model.User;

import java.io.Serializable;

public class TripShowActivity extends AppCompatActivity {

    ListView tripList ;
    Button searchBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_show);
        // objects
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("user");


        // resources
        tripList =findViewById(R.id.ListOfTrips);
        searchBtn = findViewById(R.id.Search);







        //Actions
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_home = new Intent(getApplicationContext(), TripSearch.class);
                intent_home.putExtra("user", (Serializable) user );
                startActivity(intent_home);

            }
        });
    }






}
