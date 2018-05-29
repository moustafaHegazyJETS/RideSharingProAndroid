package com.example.rania.itigraduationproject.model;

/**
 * Created by Rania on 4/25/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable {

    @SerializedName("idUser")
    @Expose
    private int idUser;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("birthDate")
    @Expose
    private String  birthDate;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("nationalid")
    @Expose
    private String nationalid;
    @SerializedName("driverCarInfo")
    @Expose
    private Object driverCarInfo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pending")
    @Expose
    private String pending;

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthDate() {
       return birthDate;
   }


    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public Object getDriverCarInfo() {
        return driverCarInfo;
    }

    public void setDriverCarInfo(Object driverCarInfo) {
        this.driverCarInfo = driverCarInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //------------------------birthdate-------
//    public String getBirthDate() {
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        String text = df.format(birthDate);
//        System.out.println("The date is: " + text);
//        return text;
//    }
}