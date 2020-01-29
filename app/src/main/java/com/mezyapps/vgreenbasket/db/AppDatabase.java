package com.mezyapps.vgreenbasket.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;

@Database(entities = {CardProductModel.class},version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract CardAppDAO getProductDAO();

     public static final Migration MIGRATION_2_3 = new Migration(1, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static AppDatabase getInStatce(Context mContext) {

         AppDatabase appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "VgreenDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
         return appDatabase;
    }
}
