package com.hovercout.sahabatlaundry.api;

import com.hovercout.sahabatlaundry.model.ResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PesananApi {

    @FormUrlEncoded
    @POST("Pesanan/tambahPesanan")
    Call<ResponseModel> tambahPesanan(
        @Field("id_customer") String id_customer,
        @Field("id_mitra") String id_mitra,
        @Field("idPesanan[]") ArrayList<String> idPesanan,
        @Field("jumlahItem[]") ArrayList<Integer> jumlahItem,
        @Field("harga") String harga,
        @Field("ongkir") Integer ongkir,
        @Field("totalHarga") Integer totalharga,
        @Field("token") String token
        );

    @FormUrlEncoded
    @POST("Pesanan/lihatPesananCustomer")
    Call<ResponseModel> lihatPesananCustomer(
            @Field("id_customer") String idCustomer
    );

    @FormUrlEncoded
    @POST("Pesanan/lihatHistoryCustomer")
    Call<ResponseModel> lihatHistoryCustomer(
            @Field("id_customer") String idCustomer
    );

    @FormUrlEncoded
    @POST("Pesanan/hapusPesanan")
    Call<ResponseModel> hapusPesanan(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("Pesanan/lihatItem")
    Call<ResponseModel> lihatItem(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("Pesanan/lihatPesananMitra")
    Call<ResponseModel> lihatPesananMitra(
            @Field("id_mitra") String id
    );

    @FormUrlEncoded
    @POST("Pesanan/terimaPesanan")
    Call<ResponseModel> terimaPesanan(
        @Field("id") String id,
        @Field("token") String token
    );

    @FormUrlEncoded
    @POST("Pesanan/tolakPesanan")
    Call<ResponseModel> tolakPesanan(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("Pesanan/selesaiPesanan")
    Call<ResponseModel> selesaiPesanan(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("Pesanan/lihatProsesMitra")
    Call<ResponseModel> lihatProsesMitra(
            @Field("id_mitra") String id
    );

    @FormUrlEncoded
    @POST("Pesanan/lihatHistoryMitra")
    Call<ResponseModel> lihatHistoryMitra(
            @Field("id_mitra") String id
    );

    @FormUrlEncoded
    @POST("Pesanan/searchCustomer")
    Call<ResponseModel> searchCustomer(
            @Field("id_mitra") String id,
            @Field("query") String query
    );
}
