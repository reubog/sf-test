package com.bognandi.sf.radinlasning;

import com.bognandi.sf.model.Adress;
import com.bognandi.sf.model.Familjemedlem;
import com.bognandi.sf.model.Person;
import com.bognandi.sf.model.Telefonuppgifter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bognandi.sf.radinlasning.RadTyp.PERSON;

public class RadHanterarTillstand {

    private HanteringObjektTyp aktuelltHanteratObjektTyp;
    private Person aktuellPerson;
    private Familjemedlem aktuellFamiljemedlem;
    private List<Person> inlastaPersoner = new ArrayList<>();
    private long antalHanteradeRader = 0;
    private long antalOkandaRader = 0;
    private long antalSkippadeRader = 0;
    private long antalFelaktigaRader = 0;
    private long antalFelaktigaPersoner = 0;
    private boolean aktuellPersonFelaktig = false;

    public void laggTillNyPerson(String[] enRadData) {
        aktuellPerson = new Person(enRadData[1], enRadData[2]);
        inlastaPersoner.add(aktuellPerson);
        aktuellFamiljemedlem = null;
        aktuelltHanteratObjektTyp = HanteringObjektTyp.PERSON;
        aktuellPersonFelaktig = false;
    }

    public void laggTillTelefonnummer(String[] enRadData) {
        if (aktuellPersonFelaktig) {
            antalSkippadeRader++;
            return;
        }

        switch (aktuelltHanteratObjektTyp) {
            case PERSON -> aktuellPerson.setTelefonuppgifter(new Telefonuppgifter(enRadData[1], enRadData[2]));
            case FAMILJEMEDLEM ->
                    aktuellFamiljemedlem.setTelefonuppgifter(new Telefonuppgifter(enRadData[1], enRadData[2]));
        }
    }

    public void laggTillAdress(String[] enRadData) {
        if (aktuellPersonFelaktig) {
            antalSkippadeRader++;
            return;
        }

        switch (aktuelltHanteratObjektTyp) {
            case PERSON ->
                    aktuellPerson.setAdress(new Adress(enRadData[1], enRadData[2], enRadData.length == 4 ? Integer.parseInt(enRadData[3]) : null));
            case FAMILJEMEDLEM ->
                    aktuellFamiljemedlem.setAdress(new Adress(enRadData[1], enRadData[2], Integer.parseInt(enRadData[3])));
        }
    }

    public void laggTillNyFamiljemedlem(String[] enRadData) {
        if (aktuellPersonFelaktig) {
            antalSkippadeRader++;
            return;
        }

        aktuellFamiljemedlem = new Familjemedlem(enRadData[1], Integer.parseInt(enRadData[2]));
        aktuellPerson.addFamiljemedlem(aktuellFamiljemedlem);
        aktuelltHanteratObjektTyp = HanteringObjektTyp.FAMILJEMEDLEM;
    }

    public void felaktigDataForPerson(String[] enRadData) {
        antalFelaktigaPersoner++;

        if (!PERSON.getRadTypKod().equals(enRadData[0])) {
            inlastaPersoner.remove(aktuellPerson);
        }

        aktuellPersonFelaktig = true;
        aktuellPerson = null;
    }

    public void okandRad() {
        antalOkandaRader++;
    }

    public void hanteradRad() {
        antalHanteradeRader++;
    }

    public void skippadRad() {
        antalSkippadeRader++;
    }

    public void felaktigRad() {
        antalFelaktigaRader++;
    }

    public long getAntalHanteradeRader() {
        return antalHanteradeRader;
    }

    public long getAntalOkandaRader() {
        return antalOkandaRader;
    }

    public long getAntalSkippadeRader() {
        return antalSkippadeRader;
    }

    public long getAntalFelaktigaRader() {
        return antalFelaktigaRader;
    }

    public long getAntalFelaktigaPersoner() {
        return antalFelaktigaPersoner;
    }

    public List<Person> getInlastaPersoner() {
        return Collections.unmodifiableList(inlastaPersoner);
    }
}
