package com.hovercout.sahabatlaundry.model;

public class ItemModel {

    private String id, namapakaian, jumlah_pakaian, id_pesanan, id_jenis_pakaian;

    public ItemModel(String id, String id_pesanan, String id_jenis_pakaian, String jumlah_pakaian, String namapakaian){
        this.id = id;
        this.id_pesanan = id_pesanan;
        this.id_jenis_pakaian = id_jenis_pakaian;
        this.jumlah_pakaian = jumlah_pakaian;
        this.namapakaian = namapakaian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamapakaian() {
        return namapakaian;
    }

    public void setNamapakaian(String namapakaian) {
        this.namapakaian = namapakaian;
    }

    public String getJumlah_pakaian() {
        return jumlah_pakaian;
    }

    public void setJumlah_pakaian(String jumlah_pakaian) {
        this.jumlah_pakaian = jumlah_pakaian;
    }

    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getId_jenis_pakaian() {
        return id_jenis_pakaian;
    }

    public void setId_jenis_pakaian(String id_jenis_pakaian) {
        this.id_jenis_pakaian = id_jenis_pakaian;
    }
}
