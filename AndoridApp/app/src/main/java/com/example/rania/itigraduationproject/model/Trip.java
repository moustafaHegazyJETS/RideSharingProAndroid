package com.example.rania.itigraduationproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trip implements Serializable {

    @SerializedName("idTrip")
    @Expose
    private Integer idTrip;
    @SerializedName("tripName")
    @Expose
    private String tripName;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("tripTime")
    @Expose
    private String time;
    @SerializedName("tripFrom")
    @Expose
    private String from;
    @SerializedName("tripTo")
    @Expose
    private String to;
    @SerializedName("numberOfSeats")
    @Expose
    private Integer numberOfSeats;
    @SerializedName("driverId")
    @Expose
    private DriverCarInfo driverId;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("dayTrip")
    @Expose
    private String day;







    public Integer getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(Integer idTrip) {
        this.idTrip = idTrip;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public DriverCarInfo getDriverId() {
        return driverId;
    }

    public void setDriverId(DriverCarInfo driverId) {
        this.driverId = driverId;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}