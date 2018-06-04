package com.example.rania.itigraduationproject.Interfaces;

import com.example.rania.itigraduationproject.model.DriverCarInfo;
import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Rania on 4/25/2018.
 */

public interface Service {
    public static final String BASE_URL = "http://10.0.2.2:8084/RideSharingProWS/rest/";

    @GET("getUser.json")
    Call<User> getUser();


    //Call<User>sendUser(@Body User user );
//    Call<User>sendUser(@Multipart Multipart image,@Multipart String user );

    @POST("user.json")
    Call<User>sendUser(@Body User user );
    //@Multipart
    //Call<User>sendUser(@Part("file") RequestBody file,@Part("user") String user);//  RequestBody user );
    @POST("getUserByEmailAndPassword.json")
    Call<User>getUserByEmailAndPassword(@Body User user);

    @POST("driverSignUpWs")
    Call<DriverCarInfo>saveDriverObject(@Body User driverCarInfo);



    @GET("getTrip/4.json")
    Call<Trip> getHello();



    @POST("getSearchTrips.json")
    Call<List<Trip>> getTripsFromSearch(@Body Trip obj );

    @GET("h")
    Call<String> getHello2();


    @POST("getDriverInfo.json")
    Call<User> getDriverInfo(@Body Trip t);

    @POST("getDriverCarInfo.json")
    Call<DriverCarInfo> getDriverCarInfo(@Body User u);

    @POST("CehckForSeats.json")
    Call<String> checkForSeats(@Body int tripID);

    @POST("registerWithTrip.json")
    Call<User> registerWithTrip(@Body List<Integer> values );

    @POST("addTrip.json")
    Call<Trip> addTrip(@Body List<Trip> vals);

    @POST("setTripToBePast.json")
    Call<Void> setTripToBePast(@Body Integer id);



}
