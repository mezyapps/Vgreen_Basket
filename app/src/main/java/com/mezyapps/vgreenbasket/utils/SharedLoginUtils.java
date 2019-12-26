package com.mezyapps.vgreenbasket.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedLoginUtils {


    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static String getLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        return preferences.getString(ConstantFields.IS_LOGIN, "");
    }

    public static void putLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_LOGIN, "true");
        editor.commit();
    }

    public static void removeLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_LOGIN, "false");
        editor.commit();
    }
    public static void addUserId(Context mContext,String user_id,String name,String mobile,String address)
    {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.USER_ID,user_id);
        editor.putString(ConstantFields.NAME,name);
        editor.putString(ConstantFields.MOBILE_NO,mobile);
        editor.putString(ConstantFields.ADDRESS,address);
        editor.commit();
    }

    public static String  getUserId(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        String user_id=preferences.getString(ConstantFields.USER_ID, "");
        return user_id;
    }
    public static String  getUserAddress(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        String address=preferences.getString(ConstantFields.ADDRESS, "");
        return address;
    }
    public static String  getUserName(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        String name=preferences.getString(ConstantFields.NAME, "");
        return name;
    }
    public static String  getUserMobile(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        String mobile_no=preferences.getString(ConstantFields.MOBILE_NO, "");
        return mobile_no;
    }
    public static void removeUserSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_LOGIN, "false");
        editor.putString(ConstantFields.USER_ID,"");
        editor.putString(ConstantFields.NAME,"");
        editor.putString(ConstantFields.MOBILE_NO,"");
        editor.commit();
    }



}
