package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.RecycleViewAdapter;
import com.example.rania.itigraduationproject.Controllers.RecyclerItemClickListener;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;
import java.util.List;

public class TripShowActivity extends AppCompatActivity {

    Button searchBtn;
    User user;
    List<Trip> tripArray;
    RecyclerView recycleView;
    RecycleViewAdapter<RecyclerView.ViewHolder> adapter;

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
        setContentView(R.layout.activity_trip_show);

        // objects
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("user");



        if(intent.getSerializableExtra("tripList")!= null)
        {
            tripArray = (List<Trip>) intent.getSerializableExtra("tripList");
            Toast.makeText(this, ""+tripArray.size(), Toast.LENGTH_SHORT).show();
        }else
        {
            //here to add no trip found code before searching
            Toast.makeText(this, "no trip", Toast.LENGTH_SHORT).show();
        }




        // resources

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
        //Recycle View Action

       if(intent.getSerializableExtra("tripList")!= null) {
           recycleView=(RecyclerView)findViewById(R.id.recyleView);
           recycleView.setHasFixedSize(true);
           recycleView.setLayoutManager(new LinearLayoutManager(this));
           adapter=new RecycleViewAdapter<RecyclerView.ViewHolder>(this,tripArray);
           recycleView.setAdapter(adapter);
           recycleView.addOnItemTouchListener(new RecyclerItemClickListener(this, recycleView ,new RecyclerItemClickListener.OnItemClickListener() {
                       @Override public void onItemClick(View view, int position) {
                           // do whatever
                             Intent in =new Intent(TripShowActivity.this,TripDetailsActivity.class);
                             in.putExtra("trip",(Serializable) tripArray.get(position));
                             in.putExtra("tripVal",(Serializable) tripArray.get(position));

                           startActivity(in);
                       }

                       @Override public void onLongItemClick(View view, int position) {
                           Intent in =new Intent(TripShowActivity.this,TripDetailsActivity.class);
                           in.putExtra("trip",(Serializable) tripArray.get(position) );
                           in.putExtra("tripVal",(Serializable) tripArray.get(position));

                           startActivity(in);
                       }
                   })
           );










       }


   }






}
