package com.mezyapps.vgreenbasket.api_common;


import com.mezyapps.vgreenbasket.model.SuccessModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(EndApi.WS_LOGIN)
    @FormUrlEncoded
    Call<SuccessModel> login(@Field("username") String mobile_no,
                             @Field("password") String password);


    @POST(EndApi.WS_PRODUCT_LIST)
    @FormUrlEncoded
    Call<SuccessModel> productList(@Field("prod_id") String prod_id);


}
