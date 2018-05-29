package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverRegister extends AppCompatActivity {
    TextView ownerCarName;
    TextView ownerCarAddress;
    TextView CarBrand;
    TextView linceEndDate;
    TextView carModel;
    TextView carColor;
    TextView carPlate;
    TextView carYearModel;
    TextView carCc;
    Button signUpBtn;
    private static Retrofit retrofit = null;

    @Override
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



        setContentView(R.layout.activity_driver_register);


        //TextView
        ownerCarName = (TextView) findViewById(R.id.car_ownername);
        ownerCarAddress = (TextView) findViewById(R.id.ownerCaraddress);
        CarBrand = (TextView) findViewById(R.id.carBrand);
        linceEndDate = (TextView) findViewById(R.id.linceEndDate);
        carPlate = (TextView) findViewById(R.id.car_plate);
        carColor = (TextView) findViewById(R.id.car_color);
        carModel = (TextView) findViewById(R.id.car_model);
        carYearModel = (TextView) findViewById(R.id.car_year_model);
        carCc = (TextView) findViewById(R.id.car_cc);
        signUpBtn = (Button) findViewById(R.id.btn_signup);
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Service service = retrofit.create(Service.class);
        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("userObj");
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DriverRegister.this,"DriverMethod", Toast.LENGTH_SHORT).show();

                final DriverCarInfo driverObject=new DriverCarInfo();
                driverObject.setCarBrand(CarBrand.getText().toString());
                driverObject.setCarCC(Integer.parseInt(carCc.getText().toString()));
                driverObject.setCarColor(carColor.getText().toString());
                driverObject.setCarModel(carModel.getText().toString());
                driverObject.setCarYear(Integer.parseInt(carYearModel.getText().toString()));
                driverObject.setDriverLicenseNum(carPlate.getText().toString());
                driverObject.setLicenseEndDate(linceEndDate.getText().toString());
                driverObject.setOwnerAddress(ownerCarAddress.getText().toString());
                driverObject.setOwnername(ownerCarName.getText().toString());
                driverObject.setNationalidPhoto("fjfj");
                driverObject.setStatus("1");
                driverObject.setLicenseIdPhoto("ffff");
                driverObject.setUser(user);
                service.saveDriverObject(driverObject).enqueue(new Callback<DriverCarInfo>() {
                    @Override
                    public void onResponse(Call<DriverCarInfo> call, Response<DriverCarInfo> response) {

                        if(response.body()!=null) {
                            Toast.makeText(DriverRegister.this, "Register suceesfully" + driverObject.getOwnername(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(DriverRegister.this, "response body  is null", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<DriverCarInfo> call, Throwable t) {
                        Toast.makeText(DriverRegister.this,"Faild", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });



}}
