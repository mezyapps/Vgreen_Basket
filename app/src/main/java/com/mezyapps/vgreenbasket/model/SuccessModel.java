package com.mezyapps.vgreenbasket.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuccessModel {
    private String message;
    private String code;

    @SerializedName("prod_list")
    private ArrayList<ProductListModel> productListModelArrayList;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<ProductListModel> getProductListModelArrayList() {
        return productListModelArrayList;
    }

    public void setProductListModelArrayList(ArrayList<ProductListModel> productListModelArrayList) {
        this.productListModelArrayList = productListModelArrayList;
    }
}
