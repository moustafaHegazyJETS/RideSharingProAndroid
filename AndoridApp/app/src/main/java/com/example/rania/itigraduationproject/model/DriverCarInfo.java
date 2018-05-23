package com.example.rania.itigraduationproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rania on 5/1/2018.
 */

public class DriverCarInfo {

    @SerializedName("driveCarID")
    @Expose
    private Integer driveCarID;
    @SerializedName("ownername")
    @Expose
    private String ownername;
    @SerializedName("driverLicenseNum")
    @Expose
    private String driverLicenseNum;
    @SerializedName("ownerAddress")
    @Expose
    private String ownerAddress;
    @SerializedName("licenseEndDate")
    @Expose
    private String licenseEndDate;
    @SerializedName("carBrand")
    @Expose
    private String carBrand;
    @SerializedName("carModel")
    @Expose
    private String carModel;
    @SerializedName("carYear")
    @Expose
    private Integer carYear;
    @SerializedName("carCC")
    @Expose
    private Integer carCC;
    @SerializedName("carColor")
    @Expose
    private String carColor;
    @SerializedName("nationalidPhoto")
    @Expose
    private String nationalidPhoto;
    @SerializedName("licenseIdPhoto")
    @Expose
    private String licenseIdPhoto;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userId")
    @Expose
    private User user;

    public Integer getDriveCarID() {
        return driveCarID;
    }

    public void setDriveCarID(Integer driveCarID) {
        this.driveCarID = driveCarID;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getDriverLicenseNum() {
        return driverLicenseNum;
    }

    public void setDriverLicenseNum(String driverLicenseNum) {
        this.driverLicenseNum = driverLicenseNum;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getLicenseEndDate() {
        return licenseEndDate;
    }

    public void setLicenseEndDate(String licenseEndDate) {
        this.licenseEndDate = licenseEndDate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCarYear() {
        return carYear;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public Integer getCarCC() {
        return carCC;
    }

    public void setCarCC(Integer carCC) {
        this.carCC = carCC;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getNationalidPhoto() {
        return nationalidPhoto;
    }

    public void setNationalidPhoto(String nationalidPhoto) {
        this.nationalidPhoto = nationalidPhoto;
    }

    public String getLicenseIdPhoto() {
        return licenseIdPhoto;
    }

    public void setLicenseIdPhoto(String licenseIdPhoto) {
        this.licenseIdPhoto = licenseIdPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public User user() {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }


}
