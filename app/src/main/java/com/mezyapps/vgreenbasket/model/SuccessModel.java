package com.mezyapps.vgreenbasket.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuccessModel {
    String code;
    String msg;

    @SerializedName("prod_list")
    private ArrayList<ProductListModel> productListModelArrayList;

    @SerializedName("location_list")
    private ArrayList<LocationModel> locationModelArrayList;

    @SerializedName("route_list")
    private ArrayList<RouteModel> routeModelArrayList;

    @SerializedName("sing_up")
    private ArrayList<UserProfileModel> userProfileSignupModelArrayList;

    @SerializedName("login")
    private ArrayList<UserProfileModel> userProfileLoginModelArrayList;

    public ArrayList<UserProfileModel> getUserProfileLoginModelArrayList() {
        return userProfileLoginModelArrayList;
    }

    public void setUserProfileLoginModelArrayList(ArrayList<UserProfileModel> userProfileLoginModelArrayList) {
        this.userProfileLoginModelArrayList = userProfileLoginModelArrayList;
    }

    public ArrayList<LocationModel> getLocationModelArrayList() {
        return locationModelArrayList;
    }

    public ArrayList<UserProfileModel> getUserProfileSignupModelArrayList() {
        return userProfileSignupModelArrayList;
    }

    public void setUserProfileSignupModelArrayList(ArrayList<UserProfileModel> userProfileSignupModelArrayList) {
        this.userProfileSignupModelArrayList = userProfileSignupModelArrayList;
    }

    public ArrayList<RouteModel> getRouteModelArrayList() {
        return routeModelArrayList;
    }

    public void setRouteModelArrayList(ArrayList<RouteModel> routeModelArrayList) {
        this.routeModelArrayList = routeModelArrayList;
    }

    public void setLocationModelArrayList(ArrayList<LocationModel> locationModelArrayList) {
        this.locationModelArrayList = locationModelArrayList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ProductListModel> getProductListModelArrayList() {
        return productListModelArrayList;
    }

    public void setProductListModelArrayList(ArrayList<ProductListModel> productListModelArrayList) {
        this.productListModelArrayList = productListModelArrayList;
    }
}
