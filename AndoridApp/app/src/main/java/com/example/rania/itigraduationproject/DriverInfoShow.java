package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

public class DriverInfoShow extends AppCompatActivity {

    User user;
    TextView nameTxt;
    TextView nationIDTxt;
    TextView genderTxt;
    TextView numberTxt;


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
        setContentView(R.layout.activity_driver_info_show);

        //Objects ----------------
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("driverInfo");
        Toast.makeText(this, "Username:"+user.getUserName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Email:"+user.getEmail(), Toast.LENGTH_SHORT).show();

        //Resourses
        nameTxt = findViewById(R.id.name);
        nameTxt.setText(user.getUserName());
        nationIDTxt = findViewById(R.id.nationalID);
        nationIDTxt.setText(user.getNationalid());
        genderTxt = findViewById(R.id.gender);
        genderTxt.setText(user.getGender());
        numberTxt = findViewById(R.id.PHnumber);
        numberTxt.setText(user.getMobile());

        //Actions


    }
}
