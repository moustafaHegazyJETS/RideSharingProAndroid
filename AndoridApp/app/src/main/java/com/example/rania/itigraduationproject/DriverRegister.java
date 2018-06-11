package com.example.rania.itigraduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverRegister extends AppCompatActivity {
    EditText ownerCarName;
    EditText ownerCarAddress;
    EditText CarBrand;
    EditText linceEndDate;
    EditText carModel;
    EditText carColor;
    EditText carPlate;
    EditText carYearModel;
    EditText carCc;
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

        //resources
        ownerCarName = (EditText) findViewById(R.id.car_ownername);
        ownerCarAddress = (EditText) findViewById(R.id.ownerCaraddress);
        CarBrand = (EditText) findViewById(R.id.carBrand);
        linceEndDate = (EditText) findViewById(R.id.linceEndDate);
        carPlate = (EditText) findViewById(R.id.car_plate);
        carColor = (EditText) findViewById(R.id.car_color);
        carModel = (EditText) findViewById(R.id.car_model);
        carYearModel = (EditText) findViewById(R.id.car_year_model);
        carCc = (EditText) findViewById(R.id.car_cc);
        signUpBtn = (Button) findViewById(R.id.btn_signup);

        //objects
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Service service = retrofit.create(Service.class);
        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("userObj");

        System.out.println("***********"+user.getDriverCarInfo());

        //actions
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
//                driverObject.setUser(user);
                if(validate())
                {
                user.setDriverCarInfo(driverObject);
                service.saveDriverObject(user).enqueue(new Callback<DriverCarInfo>() {
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


            }
        });




}

    public boolean validate() {
        boolean valid = true;
        String carOwnerName= ownerCarName.getText().toString();
        String carOwnerAddress=ownerCarAddress.getText().toString();;
        String CarBrandStr=CarBrand.getText().toString();;
        String linceEndDateStr=linceEndDate.getText().toString();;
        String carPlateStr=carPlate.getText().toString();
        String carColorStr=carColor.getText().toString();
        String carModelStr=carModel.getText().toString();
        String carCcStr=carCc.getText().toString();
        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(carOwnerName);

        if (carOwnerName.isEmpty()||!matcher.matches()) {
            ownerCarName.setError("Enter Correct Name");
            ownerCarName.requestFocus();
            valid = false;
        } else {
            ownerCarName.setError(null);
            ownerCarName.requestFocus();
        }

        if (carColorStr.isEmpty()||!matcher.matches() ) {
            carColor.setError("Between 4 and 10 alphanumeric characters");
            carColor.requestFocus();
            valid = false;
        } else {
            carColor.setError(null);
            carColor.requestFocus();
        }

        if (CarBrandStr.isEmpty()||!matcher.matches() ) {
            CarBrand.setError("Enter Correct Car Brand ");
            CarBrand.requestFocus();
            valid = false;
        } else {
            CarBrand.setError(null);
            CarBrand.requestFocus();
        }

        if (carModelStr.isEmpty()||!matcher.matches() ) {
            carModel.setError("Enter correct Car Model");
            carModel.requestFocus();
            valid = false;
        } else {
            carModel.setError(null);
            carModel.requestFocus();
        }

         pattern = Pattern.compile(new String ("[\\\\d]+[A-Za-z0-9\\\\s,\\\\.]+"));
         matcher = pattern.matcher(carOwnerName);

        if (carOwnerAddress.isEmpty()||!matcher.matches() ) {
            ownerCarAddress.setError("Enter Correct Addresss");
            ownerCarAddress.requestFocus();
            valid = false;
        } else {
            ownerCarAddress.setError(null);
            ownerCarAddress.requestFocus();
        }

        pattern = Pattern.compile(new String ("(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\\\d\\\\d)"));
        matcher = pattern.matcher(linceEndDateStr);

        if (linceEndDateStr.isEmpty()||!matcher.matches()) {
            linceEndDate.setError("Please Enter correct date: 1/1/2018 ");
            linceEndDate.requestFocus();
            valid = false;
        } else {
            linceEndDate.setError(null);
            linceEndDate.requestFocus();
        }


        if (carPlateStr.isEmpty()||carPlateStr.length() < 1 || carPlateStr.length() > 8) {
            carPlate.setError("Enter Correct Plate");
            carPlate.requestFocus();
            valid = false;
        } else {
            carPlate.setError(null);
            carPlate.requestFocus();
        }


        if (carCcStr.isEmpty()||carCcStr.length() < 1 || carCcStr.length() > 5 ) {
            carCc.setError("Enter Correct Car CC 4 Numbers");
            carCc.requestFocus();
            valid = false;
        } else {
            carCc.setError(null);
            carCc.requestFocus();
        }
        
        return valid;
    }


}
