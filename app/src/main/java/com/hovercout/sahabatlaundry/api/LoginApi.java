package com.hovercout.sahabatlaundry.api;

import com.hovercout.sahabatlaundry.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseModel> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("login/logoutCustomer")
    Call<ResponseModel> logoutCustomer(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("login/logoutMitra")
    Call<ResponseModel> logoutMitra(
            @Field("id") String id
    );
}
