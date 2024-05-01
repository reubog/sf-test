package com.bognandi.sf.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String fornamn;
    private String efternamn;
    private Adress adress;
    private Telefonuppgifter telefon;
    private List<Familjemedlem> familjemedlemmar = new ArrayList<>();

    public Person(String fornamn, String efternamn) {
        this.fornamn = fornamn;
        this.efternamn = efternamn;
    }

    public String getFornamn() {
        return fornamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public Telefonuppgifter getTelefon() {
        return telefon;
    }

    public void setTelefonuppgifter(Telefonuppgifter telefon) {
        this.telefon = telefon;
    }

    public List<Familjemedlem> getFamiljemedlemmar() {
        return familjemedlemmar;
    }

    public void addFamiljemedlem(Familjemedlem familjemedlem) {
        this.familjemedlemmar.add(familjemedlem);
    }
}
