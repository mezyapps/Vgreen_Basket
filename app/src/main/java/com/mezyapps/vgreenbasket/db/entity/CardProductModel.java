package com.mezyapps.vgreenbasket.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProductTable")
public class CardProductModel {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "product_id")
    private long product_id;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "unit_id")
    private long unit_id;

    @ColumnInfo(name = "unit")
    private long unit;

    @ColumnInfo(name = "weight")
    private String weight;

    @ColumnInfo(name = "weight_id")
    private String weight_id;

    @ColumnInfo(name = "MRP")
    private double mrp;

    @ColumnInfo(name="MRP_TOTAL")
    private double mrp_total;

    @ColumnInfo(name="price")
    private double price;

    @ColumnInfo(name="price_TOTAl")
    private double price_total;

    @ColumnInfo(name = "qty")
    private long qty;

    @ColumnInfo(name = "imagepath")
    private String imagepath;

    @Ignore
    public CardProductModel() {
    }

    public CardProductModel(long id,long product_id, String product_name, long unit_id,long unit, String weight,String weight_id, double mrp, double mrp_total, double price, double price_total, long qty,String imagepath) {
        this.id=id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.unit_id = unit_id;
        this.unit = unit;
        this.weight = weight;
        this.weight_id = weight_id;
        this.mrp = mrp;
        this.mrp_total = mrp_total;
        this.price = price;
        this.price_total = price_total;
        this.qty = qty;
        this.imagepath = imagepath;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public long getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(long unit_id) {
        this.unit_id = unit_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getMrp_total() {
        return mrp_total;
    }

    public void setMrp_total(double mrp_total) {
        this.mrp_total = mrp_total;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    public void setPrice_total(long price_total) {
        this.price_total = price_total;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public long getUnit() {
        return unit;
    }

    public void setUnit(long unit) {
        this.unit = unit;
    }

    public String getWeight_id() {
        return weight_id;
    }

    public void setWeight_id(String weight_id) {
        this.weight_id = weight_id;
    }

}
