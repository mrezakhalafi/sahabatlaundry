package com.hovercout.sahabatlaundry.api;

import com.hovercout.sahabatlaundry.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CustomerApi {

    @FormUrlEncoded
    @POST("Customer/tambahCustomer")
    Call<ResponseModel> tambahCustomer(
            @Field("nama_customer") String nama_customer,
            @Field("email") String email,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("nomor_handphone") String nomor_handphone);

    @FormUrlEncoded
    @POST("Customer/ubahCustomer")
    Call<ResponseModel> ubahCustomer(
            @Field("id") String id,
            @Field("nama_customer") String nama_customer,
            @Field("email") String email,
            @Field("alamat") String alamat,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("nomor_handphone") String nomor_handphone);

    @FormUrlEncoded
    @POST("Customer/ubahPassword")
    Call<ResponseModel> ubahPassword(
            @Field("id") String id,
            @Field("password") String password
    );
}
