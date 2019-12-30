package com.mezyapps.vgreenbasket.api_common;


import com.mezyapps.vgreenbasket.model.SuccessModel;

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


}
