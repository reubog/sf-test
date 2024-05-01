package com.bognandi.sf.model;

public class Adress {
    private String gata;
    private String stad;
    private Integer postnummer;

    public Adress(String gata, String stad, Integer postnummer) {
        this.gata = gata;
        this.stad = stad;
        this.postnummer = postnummer;
    }

    public String getGata() {
        return gata;
    }

    public String getStad() {
        return stad;
    }

    public Integer getPostnummer() {
        return postnummer;
    }
}
