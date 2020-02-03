package com.mezyapps.vgreenbasket.api_common;


import com.mezyapps.vgreenbasket.model.SuccessModel;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(EndApi.LOGIN)
    @FormUrlEncoded
    Call<SuccessModel> login(@Field("mobile_no") String mobile_no,
                             @Field("password") String password);

    @POST(EndApi.LOCATION)
    Call<SuccessModel> locationList();

    @POST(EndApi.ROUTE)
    @FormUrlEncoded
    Call<SuccessModel> routeList(@Field("id") String location_id);

    @POST(EndApi.SIGN_UP)
    @FormUrlEncoded
    Call<SuccessModel> signUp(@Field("name") String name,
                              @Field("mobile_no") String mobile_no,
                              @Field("address") String address,
                              @Field("password") String password,
                              @Field("location_id") String location_id,
                              @Field("route_id") String route_id);


    @POST(EndApi.PRODUCT_LIST)
    @FormUrlEncoded
    Call<SuccessModel> productList(@Field("prod_type_id") String prod_id);


    @POST(EndApi.NOTIFICATION)
    Call<SuccessModel> notificationList();

    @POST(EndApi.UPDATE_USER_PROFILE)
    @FormUrlEncoded
    Call<SuccessModel> updateUserProfile(
                              @Field("user_id") String user_id,
                              @Field("name") String name,
                              @Field("mobile_no") String mobile_no,
                              @Field("address") String address,
                              @Field("location_id") String location_id,
                              @Field("route_id") String route_id);

    @POST(EndApi.PLACE_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> callPlaceOrder(
            @Field("user_id") String user_id,
            @Field("product_id") JSONArray product_id,
            @Field("unit") JSONArray unit_id,
            @Field("weight_id") JSONArray weight_id,
            @Field("total_mrp") JSONArray total_mrp,
            @Field("total_price") JSONArray total_price,
            @Field("qty") JSONArray qty,
            @Field("payment_type") String payment_type,
            @Field("is_address") String is_address,
            @Field("address") String address);

    @POST(EndApi.ORDER_HISTORY_HD)
    @FormUrlEncoded
    Call<SuccessModel> orderHistoryHD(@Field("user_id") String user_id);

    @POST(EndApi.ORDER_HISTORY_DT)
    @FormUrlEncoded
    Call<SuccessModel> orderHistoryDT(@Field("order_id") String order_id);

    @POST(EndApi.CANCEL_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> cancelOrder(@Field("order_id") String order_id,
                                   @Field("user_id") String user_id,
                                   @Field("reason") String reason);

    @POST(EndApi.WS_CHANGE_PASSWORD)
    @FormUrlEncoded
    Call<SuccessModel> chnagePassword(@Field("user_id") String user_id,
                                      @Field("password") String password);
}
