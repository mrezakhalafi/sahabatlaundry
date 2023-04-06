package com.hovercout.sahabatlaundry.model;

import java.util.List;

public class ResponseModel {
    private String kode, pesan;
    private List<MitraModel> listMitra;
    private List<CustomerModel> listCustomer;
    private List<PakaianModel> listPakaian;
    private List<PesananModel> listPesanan;
    private List<ItemModel> listItem;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<MitraModel> getListMitra() {
        return listMitra;
    }

    public void setListMitra(List<MitraModel> listMitra) {
        this.listMitra = listMitra;
    }

    public List<CustomerModel> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(List<CustomerModel> listCustomer) {
        this.listCustomer = listCustomer;
    }

    public List<PakaianModel> getListPakaian() {
        return listPakaian;
    }

    public void setListPakaian(List<PakaianModel> listPakaian) {
        this.listPakaian = listPakaian;
    }

    public List<PesananModel> getListPesanan() {
        return listPesanan;
    }

    public void setListPesanan(List<PesananModel> listPesanan) {
        this.listPesanan = listPesanan;
    }

    public List<ItemModel> getListItem() {
        return listItem;
    }

    public void setListItem(List<ItemModel> listItem) {
        this.listItem = listItem;
    }
}
