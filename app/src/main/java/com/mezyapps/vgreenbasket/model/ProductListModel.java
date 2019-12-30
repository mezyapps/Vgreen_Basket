package com.mezyapps.vgreenbasket.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductListModel {
    private String prod_id;
    private String prod_code;
    private String prod_name;
    private String prod_type_id;
    private String prod_desctption;
    private String prod_image;


    @SerializedName("unit_list")
    private ArrayList<ProductUnitModel> productUnitModelArrayList;


    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getProd_name() {
        return prod_name;
    }

    public ArrayList<ProductUnitModel> getProductUnitModelArrayList() {
        return productUnitModelArrayList;
    }

    public void setProductUnitModelArrayList(ArrayList<ProductUnitModel> productUnitModelArrayList) {
        this.productUnitModelArrayList = productUnitModelArrayList;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_type_id() {
        return prod_type_id;
    }

    public void setProd_type_id(String prod_type_id) {
        this.prod_type_id = prod_type_id;
    }

    public String getProd_desctption() {
        return prod_desctption;
    }

    public void setProd_desctption(String prod_desctption) {
        this.prod_desctption = prod_desctption;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }
}
