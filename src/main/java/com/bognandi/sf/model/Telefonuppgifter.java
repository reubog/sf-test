package com.bognandi.sf.model;

public class Telefonuppgifter {
    private String mobilnummer;
    private String fastnatsnummer;

    public Telefonuppgifter(String mobilnummer, String fastnatsnummer) {
        this.mobilnummer = mobilnummer;
        this.fastnatsnummer = fastnatsnummer;
    }

    public String getMobilnummer() {
        return mobilnummer;
    }

    public String getFastnatsnummer() {
        return fastnatsnummer;
    }
}
