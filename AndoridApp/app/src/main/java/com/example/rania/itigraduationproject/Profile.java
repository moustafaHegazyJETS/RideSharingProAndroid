package com.example.rania.itigraduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.rania.itigraduationproject.model.User;

public class Profile extends AppCompatActivity {
    EditText userName;
    EditText password;
    EditText phone;
    EditText email;
    EditText nationalId;
    EditText birthdate ;
    EditText gender;
    private ProgressDialog loading;
   // EditText registerType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent=getIntent();
        //Edit text Filed -----------------------------------------------------
        User user=(User) intent.getSerializableExtra("user");
        userName=(EditText)findViewById(R.id.userNameProfEdt);
        email=(EditText)findViewById(R.id.userEmailProfEdt);
        password=(EditText)findViewById(R.id.passwordProfEdt);
        phone=(EditText)findViewById(R.id.phoneEdt);
        nationalId=(EditText)findViewById(R.id.nationalIdProfEdt);
        birthdate=(EditText)findViewById(R.id.dateEdt);
        gender=(EditText)findViewById(R.id.genderEdt);

//        loading = new ProgressDialog(this);
//        loading.setIndeterminate(true);
//        loading.setCancelable(false);
//        loading.setCanceledOnTouchOutside(false);
        //registerType=(EditText)findViewById(R.id.registerTypeEdt);
        //Set value to  Edit Text  Field-------------
        if (user!=null) {
            userName.setText(user.getUserName());
            email.setText(user.getEmail());
            password.setText(user.getPassword());
            phone.setText(user.getMobile());
            nationalId.setText(user.getNationalid());
            birthdate.setText(user.getBirthDate());
            gender.setText(user.getGender());
        }
//        registerType.setText(user.get);




    }
    public void UpdateView(User user)
    {
        if (user!=null) {
            userName.setText(user.getUserName());
            email.setText(user.getEmail());
            password.setText(user.getPassword());
            phone.setText(user.getMobile());
            nationalId.setText(user.getNationalid());
            birthdate.setText(user.getBirthDate());
            gender.setText(user.getGender());
        }

    }
}
