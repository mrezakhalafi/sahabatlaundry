package com.hovercout.sahabatlaundry.model;

public class PesananModel {
    private String id, id_customer, id_mitra, harga, tgl_pesanan, status, nama_mitra, ongkir, totalHarga, nama_customer, alamat, latitude, longitude, token, nomor_handphone;

    public PesananModel(String id, String id_customer, String id_mitra, String harga, String tgl_pesanan, String status, String nama_mitra, String ongkir, String totalHarga, String nama_customer, String alamat, String latitude, String longitude, String token, String nomor_handphone){
        this.id = id;
        this.id_customer = id_customer;
        this.id_mitra = id_mitra;
        this.harga = harga;
        this.tgl_pesanan = tgl_pesanan;
        this.status = status;
        this.nama_mitra = nama_mitra;
        this.ongkir = ongkir;
        this.totalHarga = totalHarga;
        this.nama_customer = nama_customer;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        this.nomor_handphone = nomor_handphone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getId_mitra() {
        return id_mitra;
    }

    public void setId_mitra(String id_mitra) {
        this.id_mitra = id_mitra;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTgl_pesanan() {
        return tgl_pesanan;
    }

    public void setTgl_pesanan(String tgl_pesanan) {
        this.tgl_pesanan = tgl_pesanan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama_mitra;
    }

    public void setNama(String nama) {
        this.nama_mitra = nama;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getNama_lengkap() {
        return nama_customer;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_customer = nama_lengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longitude;
    }

    public void setLongtitude(String longtitude) {
        this.longitude = longtitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomor_handphone() {
        return nomor_handphone;
    }

    public void setNomor_handphone(String nomor_handphone) {
        this.nomor_handphone = nomor_handphone;
    }
}
