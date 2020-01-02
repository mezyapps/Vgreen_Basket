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

}
