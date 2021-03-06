package com.mezyapps.vgreenbasket.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuccessModel {
    private  String code;
    private String msg;
    private String folder;


    @SerializedName("location_list")
    private ArrayList<LocationModel> locationModelArrayList;

    @SerializedName("route_list")
    private ArrayList<RouteModel> routeModelArrayList;

    @SerializedName("sing_up")
    private ArrayList<UserProfileModel> userProfileSignupModelArrayList;

    @SerializedName("login")
    private ArrayList<UserProfileModel> userProfileLoginModelArrayList;


    @SerializedName("user_profile")
    private ArrayList<UserProfileModel> userProfileUpdateLoginModelArrayList;

    @SerializedName("product_list")
    private ArrayList<ProductListModel> productListModelArrayList;

    @SerializedName("notification_list")
    private ArrayList<NotificationModel> notificationModelArrayList;


    @SerializedName("order_history_ht")
    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList;

    @SerializedName("order_head_list")
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList;


    public ArrayList<UserProfileModel> getUserProfileUpdateLoginModelArrayList() {
        return userProfileUpdateLoginModelArrayList;
    }

    public void setUserProfileUpdateLoginModelArrayList(ArrayList<UserProfileModel> userProfileUpdateLoginModelArrayList) {
        this.userProfileUpdateLoginModelArrayList = userProfileUpdateLoginModelArrayList;
    }

    public ArrayList<UserProfileModel> getUserProfileLoginModelArrayList() {
        return userProfileLoginModelArrayList;
    }

    public ArrayList<OrderHistoryDTModel> getOrderHistoryDTModelArrayList() {
        return orderHistoryDTModelArrayList;
    }

    public void setOrderHistoryDTModelArrayList(ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList) {
        this.orderHistoryDTModelArrayList = orderHistoryDTModelArrayList;
    }

    public ArrayList<OrderHistoryModel> getOrderHistoryModelArrayList() {
        return orderHistoryModelArrayList;
    }

    public void setOrderHistoryModelArrayList(ArrayList<OrderHistoryModel> orderHistoryModelArrayList) {
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
    }

    public ArrayList<NotificationModel> getNotificationModelArrayList() {
        return notificationModelArrayList;
    }

    public void setNotificationModelArrayList(ArrayList<NotificationModel> notificationModelArrayList) {
        this.notificationModelArrayList = notificationModelArrayList;
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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
