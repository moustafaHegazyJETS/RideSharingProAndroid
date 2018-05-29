package com.example.rania.itigraduationproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rania.itigraduationproject.Interfaces.Service;
import com.example.rania.itigraduationproject.model.User;
import com.example.rania.itigraduationproject.remote.CheckInternetConnection;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    EditText date;
    DatePickerDialog datePickerDialog;
    private RadioGroup radio_user_DriverGroup;
    private RadioButton radio_user_DriverGroupButton;
    private RadioGroup radioGenderType;
    private RadioButton radioGenderButton;
    private static Retrofit retrofit = null;
    TextView email;
    TextView password;
    TextView mobile;
    TextView  national_id;
    TextView login_link;
    TextView name;
    TextView confirmPass;
    Button signBtn;
    ImageView personalImage;
    File image;
    @Override
    protected void onStart() {
        super.onStart();
        if(!CheckInternetConnection.isNetworkAvailable(this))
        {
            CheckInternetConnection.bulidDuligo(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("size","act result");
        if(resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            image = new File(targetUri.getPath());
            Log.i("size",String.valueOf(image.length()));
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                personalImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_sign_up);


        login_link = (TextView) findViewById(R.id.link_login);
        signBtn=(Button)findViewById(R.id.btn_signup);
        password=(TextView)findViewById(R.id.input_password);
        email=(TextView) findViewById(R.id.input_email);
        mobile=(TextView)findViewById(R.id.phone);
        national_id=(TextView)findViewById(R.id.naional_id);
        name=(TextView) findViewById(R.id.input_name);
        date = (EditText) findViewById(R.id.date);
        confirmPass=findViewById(R.id.input_password_confirm);
        personalImage=(ImageView)findViewById(R.id.personalimage);


        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Service service = retrofit.create(Service.class);

        // Radio User or Driver
        radio_user_DriverGroup = (RadioGroup) findViewById(R.id.radioUserType);
        int typeid= radio_user_DriverGroup.getCheckedRadioButtonId();
        radio_user_DriverGroupButton =(RadioButton)findViewById(typeid);
        radio_user_DriverGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radio_user_DriverGroupButton = (RadioButton) findViewById(checkedId);

              //  Toast.makeText(getApplicationContext(), radio_user_DriverGroupButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });




        //----------------------------GetGender
        radioGenderType = (RadioGroup) findViewById(R.id.radioSex);
        //radioGenderButton =(RadioButton)findViewById;
        int genid= radioGenderType.getCheckedRadioButtonId();
        radioGenderButton =(RadioButton)findViewById(genid);
        radioGenderType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radioGenderButton =(RadioButton)findViewById(checkedId);


              //  Toast.makeText(getApplicationContext(), radioGenderButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        // initiate the date picker and a button
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //Check If it is User Or Driver
//--------------------------------------------------------------------------------------
           signBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                 //  Gson g=new Gson();
                   User user = new User();
                   user.setUserName(name.getText().toString());
                   user.setPassword(password.getText().toString());
                   user.setEmail(email.getText().toString());
                   user.setGender(radioGenderButton.getText().toString());
                   user.setMobile(mobile.getText().toString());
                   user.setNationalid(national_id.getText().toString());
//                   RequestBody requestbody_image = RequestBody.create(MediaType.parse("multipart/form-data"),image);
////                   MultipartBody.Part body = MultipartBody.Part.createFormData("file",image.getName());
//
//
////                   MultipartBody.Part bodyUser = MultipartBody.Part.createFormData("user", String.valueOf(user));
//
//                   String userGson=g.toJson(user);
//                   RequestBody requestbody_User = RequestBody.create(MediaType.parse("text/plain"),userGson);

                   //user.setBirthDate(date.getText().toString());
                   if(validate()==true) {

                       if (radio_user_DriverGroupButton.getText().toString().equals("User")) {


                           service.sendUser(user).enqueue(new Callback<User>() {
                               @Override
                               public void onResponse(Call<User> call, Response<User> response) {
                                   Toast.makeText(SignUp.this, "SignUp Sucessfully", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(getApplicationContext(), Login.class);
                                   startActivity(intent);

                               }

                               @Override
                               public void onFailure(Call<User> call, Throwable t) {
                                   Toast.makeText(SignUp.this, "SignUp Faild", Toast.LENGTH_SHORT).show();
//                                   Log.i("status",t.getMessage());
                               }
                           });

                       } else if (radio_user_DriverGroupButton.getText().toString().equals("Driver")) {

                           Toast.makeText(SignUp.this, "SignUp Sucessfully", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(getApplicationContext(), DriverRegister.class);

                           intent.putExtra("userObj", (Serializable) user);
                           startActivity(intent);


                       }
                   }
               }});


//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------



//        personalImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent,0);
//            }
//
//        });


        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });


    }
    //----------------------------SignUp Validation
    public boolean validate() {
        boolean valid = true;
        String username=name.getText().toString();
        String pass=password.getText().toString();
        String useremail=email.getText().toString();
        String genderbtn=radioGenderButton.getText().toString();
        String phone=mobile.getText().toString();
        String userNamtionlId=national_id.getText().toString();
        String usertype=radio_user_DriverGroupButton.getText().toString();
        String confirmPassword=confirmPass.getText().toString();
        String userSelectDate=date.getText().toString();

        //national Id Regax
        Pattern pattern = Pattern.compile(new String ("(2)[0-9][1-9][0-1][1-9][0-3][1-9](01|02|03|04|11|12|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|31|32|33|34|35|88)\\d\\d\\d\\d\\d"));
        Matcher matcher = pattern.matcher(userNamtionlId);
        if(userNamtionlId.isEmpty()||!matcher.matches())
        {
            national_id.setError("Enter Correcct National id");
            national_id.requestFocus();
            valid = false;

        }
        else
        {

            national_id.setError(null);
            national_id.requestFocus();
        }
        pattern = Pattern.compile(new String ("^[a-zA-Z\\s]*$"));
        matcher = pattern.matcher(username);
        if (username.isEmpty()||!matcher.matches()) {
            name.setError("Enter Correct User name");
            name.requestFocus();
            valid = false;
        } else {
            name.setError(null);
            name.requestFocus();
        }
        pattern = Pattern.compile(new String ("(01)[0-9]{9}"));
        matcher = pattern.matcher(phone);
        if (phone.isEmpty() || !matcher.matches()) {
            mobile.setError("Enter a valid Phone Number");
            mobile.requestFocus();
            valid = false;
        } else {
            mobile.setError(null);
            mobile.requestFocus();
        }
        if (genderbtn.isEmpty()) {
            radioGenderButton.setError("Select Gender  Please");
            radioGenderButton.requestFocus();
            valid = false;
        } else {
            radioGenderButton.setError(null);
            radioGenderButton.requestFocus();
        }
        if (usertype.isEmpty()) {
            radio_user_DriverGroupButton.setError("Select User Type");
            radio_user_DriverGroupButton.requestFocus();
            valid = false;
        } else {
            radio_user_DriverGroupButton.setError(null);
            radio_user_DriverGroupButton.requestFocus();
        }

        if (useremail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
            email.setError("Enter a valid email address");
            email.requestFocus();
            valid = false;
        } else {
            email.setError(null);
            email.requestFocus();
        }

        pattern = Pattern.compile(new String ("(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\\\d\\\\d)"));
        matcher = pattern.matcher(userSelectDate);
        if (userSelectDate.isEmpty()||matcher.matches()) {
            date.setError("Enter Date Please");
            date.requestFocus();
            valid = false;
        } else {
            date.setError(null);
            date.requestFocus();
        }

        if (pass.isEmpty()|| pass.length() < 4 || pass.length() > 10 ) {
            password.setError("Between 4 and 10 alphanumeric characters");
            password.requestFocus();
            valid = false;
        } else {
            password.setError(null);
            password.requestFocus();
        }
        if (confirmPassword.isEmpty()||!pass.equals(confirmPassword)) {
            confirmPass.setError("Password Not matched");
            confirmPass.requestFocus();
            valid = false;

        } else
        {
            confirmPass.setError(null);
            confirmPass.requestFocus();

        }


        return valid;
    }


}
