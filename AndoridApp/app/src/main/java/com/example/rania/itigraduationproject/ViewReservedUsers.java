package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class ViewReservedUsers extends AppCompatActivity {

    List<User> userList = new ArrayList<>();
    ListView usersListView ;
    Trip trip;
    User driverUser;

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
        setContentView(R.layout.activity_view_reserved_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        userList = (List<User>) getIntent().getExtras().get("usersList");
        driverUser = (User) getIntent().getExtras().get("driverUser");
        trip = (Trip) getIntent().getExtras().get("trip");

        List<String> userListNames = new ArrayList<>();
        for (int ii =0 ; ii<userList.size() ; ii++)
        {
            userListNames.add(userList.get(ii).getUserName().toString());
        }

        //resources
        usersListView = findViewById(R.id.listOfUsers);

        usersListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1 , userListNames));




        //actions
        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ViewReservedUsers.this, "Hi", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent in =new Intent(ViewReservedUsers.this,ViewReservedUserDetails.class);
                in.putExtra("user",(Serializable) userList.get(i) );
                in.putExtra("trip",(Serializable)trip);
                in.putExtra("driverUser",(Serializable)driverUser);
                startActivity(in);

            }
        });

    }

}
