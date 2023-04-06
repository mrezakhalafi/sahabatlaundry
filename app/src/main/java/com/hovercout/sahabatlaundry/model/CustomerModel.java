package com.hovercout.sahabatlaundry.model;

public class CustomerModel {

    private String id, nama_customer, email, alamat, nomor_handphone, bergabung, latitude, longitude;

    public CustomerModel(String nama_customer, String email, String alamat, String nomor_handphone, String bergabung, String latitude, String longitude){
        this.nama_customer = nama_customer;
        this.email = email;
        this.alamat = alamat;
        this.nomor_handphone = nomor_handphone;
        this.bergabung = bergabung;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_lengkap() {
        return nama_customer;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_customer = nama_lengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomor_handphone() {
        return nomor_handphone;
    }

    public void setNomor_handphone(String nomor_handphone) {
        this.nomor_handphone = nomor_handphone;
    }

    public String getBergabung() {
        return bergabung;
    }

    public void setBergabung(String bergabung) {
        this.bergabung = bergabung;
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
}
