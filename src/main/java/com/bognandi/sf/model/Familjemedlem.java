package com.bognandi.sf.model;

public class Familjemedlem {
    private String namn;
    private int fodelsear;
    private Adress adress;
    private Telefonuppgifter telefonuppgifter;

    public Familjemedlem(String namn, int fodelsear) {
        this.namn = namn;
        this.fodelsear = fodelsear;
    }

    public String getNamn() {
        return namn;
    }

    public int getFodelsear() {
        return fodelsear;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public Telefonuppgifter getTelefonuppgifter() {
        return telefonuppgifter;
    }

    public void setTelefonuppgifter(Telefonuppgifter telefonuppgifter) {
        this.telefonuppgifter = telefonuppgifter;
    }
}
