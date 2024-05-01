package com.bognandi.sf.radinlasning;

import com.bognandi.sf.model.Adress;
import com.bognandi.sf.model.Familjemedlem;
import com.bognandi.sf.model.Person;
import com.bognandi.sf.model.Telefonuppgifter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RadHanterarTillstandTest {
    private static String FORNAMN = "elon";
    private static String EFTERNAMN = "musk";
    private static String NAMN = "elvis presley";
    private static int FODELSEAR = 1234;
    private static String TELEFONNUMMER1 = "08-123456";
    private static String TELEFONNUMMER2 = "070-1234567";
    private static String GATA = "Gatan 1";
    private static String STAD = "Staden";
    private static Integer POSTNUMMER = 12345;


    private RadHanterarTillstand tillstand;

    @BeforeEach
    void setup() {
        tillstand = new RadHanterarTillstand();
        tillstand.laggTillNyPerson(skapaStringArray("P",FORNAMN, EFTERNAMN ));
    }

    @Test
    void testLaggTillNyPerson() {
        // GIVEN
        RadHanterarTillstand tillstand = new RadHanterarTillstand(); // måste vara nollställd
        assertEquals(0, tillstand.getInlastaPersoner().size());

        // WHEN
        tillstand.laggTillNyPerson(skapaStringArray("P",FORNAMN, EFTERNAMN ));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        assertEquals(FORNAMN, person.getFornamn());
        assertEquals(EFTERNAMN, person.getEfternamn());
    }

    @Test
    void testLaggTillTelefonnummer() {
        // GIVEN
        assertEquals(1, tillstand.getInlastaPersoner().size());

        // WHEN
        tillstand.laggTillTelefonnummer(skapaStringArray("T",TELEFONNUMMER1, TELEFONNUMMER2));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        Telefonuppgifter telefonuppgifter = person.getTelefon();
        assertNotNull(telefonuppgifter);
        assertEquals(TELEFONNUMMER1, telefonuppgifter.getMobilnummer());
        assertEquals(TELEFONNUMMER2, telefonuppgifter.getFastnatsnummer());
    }

    @Test
    void testLaggTillAdress() {
        // GIVEN
        assertEquals(1, tillstand.getInlastaPersoner().size());

        // WHEN
        tillstand.laggTillAdress(skapaStringArray("A",GATA, STAD, String.valueOf(POSTNUMMER)));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        Adress adress = person.getAdress();
        assertNotNull(adress);
        assertEquals(GATA, adress.getGata());
        assertEquals(STAD, adress.getStad());
        assertEquals(POSTNUMMER, adress.getPostnummer());
    }

    @Test
    void testLaggTillFamiljemedlem() {
        // GIVEN
        assertEquals(1, tillstand.getInlastaPersoner().size());

        // WHEN
        tillstand.laggTillNyFamiljemedlem(skapaStringArray("F",NAMN, String.valueOf(FODELSEAR)));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        Familjemedlem familjemedlem = person.getFamiljemedlemmar().get(0);
        assertNotNull(familjemedlem);
        assertEquals(NAMN, familjemedlem.getNamn());
        assertEquals(FODELSEAR, familjemedlem.getFodelsear());
    }


    @Test
    void testLaggTillAdressHosFamiljemedlem() {
        // GIVEN
        tillstand.laggTillNyFamiljemedlem(skapaStringArray("F",NAMN, String.valueOf(FODELSEAR)));
        assertEquals(1, tillstand.getInlastaPersoner().size());
        assertEquals(1, tillstand.getInlastaPersoner().get(0).getFamiljemedlemmar().size());

        // WHEN
        tillstand.laggTillAdress(skapaStringArray("A",GATA, STAD, String.valueOf(POSTNUMMER)));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        Familjemedlem familjemedlem = person.getFamiljemedlemmar().get(0);
        assertNotNull(familjemedlem);
        Adress adress = familjemedlem.getAdress();
        assertNotNull(adress);
        assertEquals(GATA, adress.getGata());
        assertEquals(STAD, adress.getStad());
        assertEquals(POSTNUMMER, adress.getPostnummer());
    }

    @Test
    void testLaggTillTelefonnummerHosFamiljemedlem() {
        // GIVEN
        tillstand.laggTillNyFamiljemedlem(skapaStringArray("F",NAMN, String.valueOf(FODELSEAR)));
        assertEquals(1, tillstand.getInlastaPersoner().size());
        assertEquals(1, tillstand.getInlastaPersoner().get(0).getFamiljemedlemmar().size());

        // WHEN
        tillstand.laggTillTelefonnummer(skapaStringArray("T",TELEFONNUMMER1, TELEFONNUMMER2));

        // THEN
        Person person = tillstand.getInlastaPersoner().get(0);
        Familjemedlem familjemedlem = person.getFamiljemedlemmar().get(0);
        assertNotNull(familjemedlem);
        Telefonuppgifter telefonuppgifter = familjemedlem.getTelefonuppgifter();
        assertNotNull(telefonuppgifter);
        assertEquals(TELEFONNUMMER1, telefonuppgifter.getMobilnummer());
        assertEquals(TELEFONNUMMER2, telefonuppgifter.getFastnatsnummer());
    }

    @Test
    void testSkippadeRader() {
        // GIVEN
        assertEquals(0, tillstand.getAntalSkippadeRader());

        // WHEN
        tillstand.skippadRad();
        tillstand.skippadRad();
        tillstand.skippadRad();

        // THEN
        assertEquals(3, tillstand.getAntalSkippadeRader());
    }

    @Test
    void testFelaktigaRader() {
        // GIVEN
        assertEquals(0, tillstand.getAntalFelaktigaRader());

        // WHEN
        tillstand.felaktigRad();
        tillstand.felaktigRad();
        tillstand.felaktigRad();

        // THEN
        assertEquals(3, tillstand.getAntalFelaktigaRader());
    }

    @Test
    void testOkandaRader() {
        // GIVEN
        assertEquals(0, tillstand.getAntalOkandaRader());

        // WHEN
        tillstand.okandRad();
        tillstand.okandRad();
        tillstand.okandRad();

        // THEN
        assertEquals(3, tillstand.getAntalOkandaRader());
    }


    @Test
    void testHanteradeRader() {
        // GIVEN
        assertEquals(0, tillstand.getAntalHanteradeRader());

        // WHEN
        tillstand.hanteradRad();
        tillstand.hanteradRad();
        tillstand.hanteradRad();

        // THEN
        assertEquals(3, tillstand.getAntalHanteradeRader());
    }

    private String[] skapaStringArray(String ...args) {
        return args;
    }
}
