package com.example.rania.itigraduationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;

public class DriverProfile extends AppCompatActivity {
    EditText CarOwnerName;
    EditText driveraddress;
    EditText drivercarCC;
    EditText drivercarModel;
    EditText drivercarColor;
    EditText drivercarPlate;
    EditText drivercarBrand;
    EditText driverphone;
    EditText drivernationalId;
    EditText driverlinceEndDate;
    EditText birthDate;
    EditText drivergender;
    EditText driveremail;
    EditText driverName;


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
        setContentView(R.layout.activity_driver_profile);

        //objects
        User driverInfo=(User)getIntent().getExtras().get("driverUser");

        //Resources
         CarOwnerName=findViewById(R.id.driverCarOwnerNameEdt);
         driveraddress=findViewById(R.id.driverCarOwneraddressEdt);
         drivercarCC=findViewById(R.id.driverCarCCEdt);
         drivercarModel=findViewById(R.id.CarModlEdt);
         drivercarColor=findViewById(R.id.driverCarcolorEdt);
         drivercarPlate=findViewById(R.id.CarplateEdt);
         drivercarBrand=findViewById(R.id.CarbrandEdt);
         driverphone=findViewById(R.id.driverphoneEdt);
         drivernationalId=findViewById(R.id.drivernationalIdProfEdt);
         driverlinceEndDate=findViewById(R.id.CarendlinceDateEdt);
         birthDate=findViewById(R.id.driverdateEdt);
         drivergender=findViewById(R.id.drivergenderEdt);
         driveremail=findViewById(R.id.driverEmailProfEdt);
         driverName=findViewById(R.id.driverNameProfEdt);


         //SetValues
        if(driverInfo!=null) {
            CarOwnerName.setText(driverInfo.getDriverCarInfo().getOwnername());
            driveraddress.setText(driverInfo.getDriverCarInfo().getOwnerAddress());
            drivercarCC.setText(Integer.toString(driverInfo.getDriverCarInfo().getCarCC()));
            drivercarModel.setText(driverInfo.getDriverCarInfo().getCarModel());
            drivercarColor.setText(driverInfo.getDriverCarInfo().getCarColor());
            drivercarPlate.setText(driverInfo.getDriverCarInfo().getDriverLicenseNum());
            drivercarBrand.setText(driverInfo.getDriverCarInfo().getCarBrand());
            driverphone.setText(driverInfo.getMobile());
            drivernationalId.setText(driverInfo.getNationalid());
            driverlinceEndDate.setText(driverInfo.getDriverCarInfo().getLicenseEndDate());
            birthDate.setText(driverInfo.getBirthDate());
            drivergender.setText(driverInfo.getGender());
            driveremail.setText(driverInfo.getEmail());
            driverName.setText(driverInfo.getUserName());
        }
        else
        {
            Toast.makeText(this, "Error In Loadding", Toast.LENGTH_SHORT).show();
        }



    }
}
