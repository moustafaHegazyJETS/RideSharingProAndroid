package com.example.rania.itigraduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.rania.itigraduationproject.model.User;

public class Profile extends AppCompatActivity {
    EditText userName;
    EditText phone;
    EditText email;
    EditText nationalId;
    EditText birthdate ;
    EditText gender;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent=getIntent();
        //Edit text Filed -----------------------------------------------------
        User user=(User) intent.getSerializableExtra("user");
        userName=(EditText)findViewById(R.id.userNameProfEdt);
        email=(EditText)findViewById(R.id.userEmailProfEdt);
        phone=(EditText)findViewById(R.id.phoneEdt);
        nationalId=(EditText)findViewById(R.id.nationalIdProfEdt);
        birthdate=(EditText)findViewById(R.id.dateEdt);
        gender=(EditText)findViewById(R.id.genderEdt);

        UpdateView(user);
    }


    public void UpdateView(User user)
    {
        if (user!=null) {
            userName.setText(user.getUserName());
            email.setText(user.getEmail());
            phone.setText(user.getMobile());
            nationalId.setText(user.getNationalid());
            birthdate.setText(user.getBirthDate());
            gender.setText(user.getGender());
        }

    }
}
