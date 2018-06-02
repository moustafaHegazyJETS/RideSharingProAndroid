package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.rania.itigraduationproject.model.User;

public class DriverHomeActivity extends AppCompatActivity {

    TextView nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //objects
        Intent intent = getIntent();
        User user = (User) intent.getExtras().get("user");



        //resources
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        nameTxt = findViewById(R.id.driverName);
        nameTxt.setText(user.getUserName());




        //actions
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
