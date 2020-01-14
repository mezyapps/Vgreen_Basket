package com.mezyapps.vgreenbasket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistoryModel implements Parcelable {

    private String id;
    private String user_id;
    private String total_price;
    private String total_mrp;
    private String order_id;
    private String status;
    private String date;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {


        @Override
        public OrderHistoryModel createFromParcel(Parcel source) {
            return new OrderHistoryModel(source);
        }

        @Override
        public OrderHistoryModel[] newArray(int size) {
            return new OrderHistoryModel[0];
        }
    };

    public OrderHistoryModel(Parcel source) {
        this.id = source.readString();
        this.user_id = source.readString();
        this.total_price = source.readString();
        this.total_mrp = source.readString();
        this.order_id = source.readString();
        this.status = source.readString();
        this.date = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.user_id);
        dest.writeString(this.total_price);
        dest.writeString(this.total_mrp);
        dest.writeString(this.order_id);
        dest.writeString(this.status);
        dest.writeString(this.date);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_mrp() {
        return total_mrp;
    }

    public void setTotal_mrp(String total_mrp) {
        this.total_mrp = total_mrp;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}