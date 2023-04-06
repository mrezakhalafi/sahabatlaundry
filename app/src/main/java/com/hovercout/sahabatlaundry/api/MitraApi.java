package com.hovercout.sahabatlaundry.api;

import com.hovercout.sahabatlaundry.model.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MitraApi {

    @FormUrlEncoded
    @POST("Mitra/tambahMitra")
    Call<ResponseModel> tambahMitra(
            @Field("nama_mitra") String nama_mitra,
            @Field("email") String email,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("jam_buka") String jam_buka,
            @Field("jam_tutup") String jam_tutup,
            @Field("nomor_handphone") String nomor_handphone);

    @GET("Mitra/lihatMitra")
    Call<ResponseModel> lihatMitra();

    @FormUrlEncoded
    @POST("Mitra/lihatLayanan")
    Call<ResponseModel> lihatLayanan(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("Mitra/SearchMitra")
    Call<ResponseModel> searchMitra(
            @Field("query") String query
    );

    @Multipart
    @POST("Mitra/tambahLayanan")
    Call<ResponseModel> tambahLayanan(
            @Part("id_mitra") RequestBody id_mitra,
            @Part("namapakaian") RequestBody namapakaian,
            @Part("hargapakaian") RequestBody hargapakaian,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("Mitra/tambahLayananNoFoto")
    Call<ResponseModel> tambahLayananNoFoto(
            @Field("id_mitra") String id_mitra,
            @Field("namapakaian") String namapakaian,
            @Field("hargapakaian") String hargapakaian
    );

    @FormUrlEncoded
    @POST("Mitra/hapusLayanan")
    Call<ResponseModel> hapusLayanan(
        @Field("id") String id
    );

    @Multipart
    @POST("Mitra/ubahLayanan")
    Call<ResponseModel> ubahLayanan(
            @Part("id") RequestBody id,
            @Part("idMitra") RequestBody idMitra,
            @Part("namapakaian") RequestBody namapakaian,
            @Part("hargapakaian") RequestBody hargapakaian,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("Mitra/ubahLayananNoFoto")
    Call<ResponseModel> ubahLayananNoFoto(
            @Field("id") String id,
            @Field("namapakaian") String namapakaian,
            @Field("hargapakaian") String hargapakaian
    );

    @FormUrlEncoded
    @POST("Mitra/ubahPassword")
    Call<ResponseModel> ubahPassword(
            @Field("id") String id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("Mitra/ubahMitra")
    Call<ResponseModel> ubahMitra(
            @Field("id") String id,
            @Field("nama_mitra") String nama_mitra,
            @Field("email") String email,
            @Field("alamat") String alamat,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("jam_buka") String jam_buka,
            @Field("jam_tutup") String jam_tutup,
            @Field("nomor_handphone") String nomor_handphone
    );

    @Multipart
    @POST("Mitra/uploadFoto")
    Call<ResponseModel> uploadFoto(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part image
            );
}
