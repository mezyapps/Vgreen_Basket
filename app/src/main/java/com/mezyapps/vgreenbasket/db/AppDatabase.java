package com.mezyapps.vgreenbasket.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;

@Database(entities = {CardProductModel.class},version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract CardAppDAO getProductDAO();
}
