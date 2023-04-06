package com.hovercout.sahabatlaundry.model;

public class MitraModel {

    private String id, nama_mitra, email, password, alamat, jam_buka, jam_tutup, nomor_handphone, bergabung, latitude, longitude, foto, token;
    private Float jarak;

    public MitraModel(String id, String nama_mitra, String email, String password, String alamat, String jam_buka, String jam_tutup, String nomor_handphone, String bergabung, String latitude, String longitude, String foto, String token, Float jarak){
        this.id = id;
        this.nama_mitra = nama_mitra;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.nomor_handphone = nomor_handphone;
        this.bergabung = bergabung;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.token = token;
        this.jarak = jarak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama_mitra;
    }

    public void setNama(String nama) {
        this.nama_mitra = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Float getJarak() {
        return jarak;
    }

    public void setJarak(Float jarak) {
        this.jarak = jarak;
    }
}
