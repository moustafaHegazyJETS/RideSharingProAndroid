package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Controllers.SessionManager;
import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    TextView sign_link;
    TextView email_text;
    TextView password_text;
    Button login_btn;
    String email;
    String password;
    private static Retrofit retrofit = null;
    SessionManager session_mangement;
    Service service;




    @Override
    protected void onStart() {
        super.onStart();

        if (!CheckInternetConnection.isNetworkAvailable(this)) {

            CheckInternetConnection.bulidDuligo(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

         //---------------SharedPrefrences ----------
        session_mangement = new SessionManager(getApplicationContext());


        //Retrofit -----------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         service = retrofit.create(Service.class);
        //------------------------------------------------

        sign_link = (TextView) findViewById(R.id.link_signup);
        email_text = (TextView) findViewById(R.id.email);
        password_text = (TextView) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.login_btn);

        //Singn_Link Action
         sign_link.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getApplicationContext(),SignUp.class);
                 startActivity(intent);
             }
         });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    User user =new User();
                    user.setEmail(email_text.getText().toString());
                    user.setPassword(password_text.getText().toString());
                    user.setDriverCarInfo(null);
                    requestUser(user);
                }
                else
                {
                    Toast.makeText(Login.this,"error",Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    public boolean validate() {
        boolean valid = true;

        email = email_text.getText().toString();
        password = password_text.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_text.setError("Enter a valid email address");
            email_text.requestFocus();
            valid = false;
        } else {
            email_text.setError(null);
            email_text.requestFocus();
        }

        if (password.isEmpty()|| password.length() < 4 || password.length() > 10 ) {
            password_text.setError("Between 4 and 10 alphanumeric characters");
            password_text.requestFocus();
            valid = false;
        } else {
            password_text.setError(null);
            password_text.requestFocus();
        }


        return valid;
    }
    public void requestUser(final User user)
    {
        service.getUserByEmailAndPassword(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.body().getEmail()!=null ){


                    if(response.body().getPending().equals("-1")){
                        Toast.makeText(Login.this,"Account has been blocked",Toast.LENGTH_LONG).show();

                    }else{
                        session_mangement.createLoginSession(response.body().getEmail(),response.body().getPassword());
                        if(response.body().getDriverCarInfo()!=null){
                            Intent driver_i = new Intent(Login.this, HomeDriver.class);
                            driver_i.putExtra("user", response.body());
                            startActivity(driver_i);
                            finish();

                        }else
                        {
                            Intent intent_home = new Intent(getApplicationContext(), HomeUser.class);
                            intent_home.putExtra("user", (Serializable) response.body());
                            startActivity(intent_home);
                            finish();
                        }
                    }




                }else{
                    Toast.makeText(Login.this,"invalid email or password",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Login.this,"faild interent connection ",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
