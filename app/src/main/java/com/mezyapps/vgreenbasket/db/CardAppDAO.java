package com.mezyapps.vgreenbasket.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mezyapps.vgreenbasket.db.entity.CardProductModel;

import java.util.List;

@Dao
public interface CardAppDAO {
    @Insert
    public long addProduct(CardProductModel cardProductModel);

    @Update
    public void updateProduct(CardProductModel cardProductModel);

    @Delete
    public void  deleteProduct(CardProductModel cardProductModel);

    @Query("select * from ProductTable")
    public List<CardProductModel> getAppProduct();

    @Query("select * from ProductTable where product_id ==:product_id")
    public CardProductModel getProduct(long product_id);

    @Query("UPDATE ProductTable SET qty = :qty WHERE product_id = :product_id")
    public long getProductQtyUpdate(long qty,long product_id);

    @Query("UPDATE ProductTable SET qty = :qty,price_TOTAl = :price_total,MRP_TOTAL = :mrp_total WHERE product_id = :product_id")
    public long getProductDataUpdate(long qty,long product_id,long price_total,long mrp_total);
}
