package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripShowActivity extends AppCompatActivity {

    ListView tripList ;
    Button searchBtn;
    User user;
    List<Trip> tripArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_show);

        // objects
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("user");

        if(intent.getSerializableExtra("tripList")!= null)
        {
            tripArray = (List<Trip>) intent.getSerializableExtra("tripList");
        }else
        {
            //here to add no trip found code before searching
        }




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

                        //list
        if(intent.getSerializableExtra("tripList")!= null) {


            List<String> tripNames = new ArrayList<>();
            for (int i =0 ; i<tripArray.size() ; i++)
            {
                tripNames.add(i,tripArray.get(i).getTripName().toString());

            }

            tripList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripNames));

            tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent in =new Intent(TripShowActivity.this,TripDetailsActivity.class);
                    in.putExtra("trip",(Serializable) tripArray.get(i) );
                    startActivity(in);

                }
            });
        }

    }






}
