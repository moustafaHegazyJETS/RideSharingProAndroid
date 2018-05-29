package com.example.rania.itigraduationproject.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.rania.itigraduationproject.Login;

import java.util.HashMap;

public class SessionManager {


        SharedPreferences pref;


        SharedPreferences.Editor editor;


        Context _context;


        int PRIVATE_MODE = 0;


        private static final String PREF_NAME = "login_user_data";


        private static final String IS_LOGIN = "IsLoggedIn";


        public static final String KEY_password = "name";


        public static final String KEY_EMAIL = "email";


        public SessionManager(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }


        public void createLoginSession( String email,String password){

            System.out.println(email);

            editor.putBoolean(IS_LOGIN, true);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_password,password);
            editor.commit();
        }


        public void checkLogin(){

            if(!this.isLoggedIn()){

                Intent i = new Intent(_context, Login.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                _context.startActivity(i);
            }

        }





        public void logoutUser(){

            editor.clear();
            editor.commit();


            Intent i = new Intent(_context, Login.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            _context.startActivity(i);
        }


        public boolean isLoggedIn(){
            return pref.getBoolean(IS_LOGIN, false);
        }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        // user password
        user.put(KEY_password, pref.getString(KEY_password, null));
        // return user
        return user;
    }


    }



