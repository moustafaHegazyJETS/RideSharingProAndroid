package com.example.rania.itigraduationproject.remote;

import com.example.rania.itigraduationproject.Interfaces.Service;

import retrofit2.Retrofit;

public class APIUtils {
    private APIUtils(){}
    public static final String API_URL = "http://10.0.2.2:8084/RideSharingProWS/rest/user";
    public static Service getService(){
        return RetrofitClient.getClient(API_URL).create(Service.class);
    }
}
