package com.hovercout.sahabatlaundry.model;

public class PakaianModel {
    private String id, namapakaian, hargapakaian, foto;

    public PakaianModel(String id, String namapakaian, String hargapakaian, String foto){
        this.id = id;
        this.namapakaian = namapakaian;
        this.hargapakaian = hargapakaian;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaPakaian() {
        return namapakaian;
    }

    public void setNamaPakaian(String namaPakaian) {
        this.namapakaian = namaPakaian;
    }

    public String getHargaPakaian() {
        return hargapakaian;
    }

    public void setHargaPakaian(String hargaPakaian) {
        this.hargapakaian = hargaPakaian;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
