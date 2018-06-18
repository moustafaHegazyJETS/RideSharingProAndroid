package com.example.rania.itigraduationproject.Interfaces;

import com.example.rania.itigraduationproject.model.Trip;
import com.example.rania.itigraduationproject.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TripService {
    public static final String BASE_URL = "http://192.168.1.6:8080/RideSharingProWS/rest/";

    @POST("getAllUserReserverdTrips.json")
    Call<List<Trip>> getAllUserReserverdTrips(@Body User user );
}
