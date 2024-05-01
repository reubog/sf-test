package com.bognandi.sf.radinlasning;

import com.bognandi.sf.radinlasning.validering.RadHanterarValiderare;
import com.bognandi.sf.radinlasning.validering.ValideringException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class RadHanterareTest {
    private static long RADNUMMER = 123;
    private static String TOM_RAD_SOM_SKIPPAS = "";
    private static String OKAND_RAD = "ölkjhgfdsa";
    private static String HANTERAD_RAD = "P|adam|bertilsson";
    private static String FELAKTIG_RAD = "T|";
    private static String PERSON_RAD = "P|asdf|qwer";
    private static String TELEFON_RAD = "T|08-123456|070-12345678";
    private static String ADRESS_RAD = "A|Gata 1|Stad|12345";
    private static String FAMILJ_RAD = "F|namn|1234";

    private RadHanterarValiderare validerare;
    private RadHanterarTillstand tillstand;
    private RadHanterare hanterare;

    @BeforeEach
    void setup() {
        validerare = mock(RadHanterarValiderare.class);
        tillstand = mock(RadHanterarTillstand.class);
        hanterare = new RadHanterare(validerare);
    }

    @Test
    void testSkippadRad() {
        // WHEN
        hanterare.hanteraRad(tillstand, TOM_RAD_SOM_SKIPPAS, RADNUMMER);

        // THEN
        verify(tillstand).skippadRad();
    }

    @Test
    void testOkandRad() {
        // WHEN
        hanterare.hanteraRad(tillstand, OKAND_RAD, RADNUMMER);

        // THEN
        verify(tillstand).okandRad();
    }

    @Test
    void testHanteradRad() {
        // WHEN
        hanterare.hanteraRad(tillstand, HANTERAD_RAD, RADNUMMER);

        // THEN
        verify(tillstand).hanteradRad();
    }

    @Test
    void testFelaktigRad() {
        // GIVEN
        doThrow(ValideringException.class).when(validerare).kontrolleraTelefonnummerData(any(String[].class));

        // WHEN
        hanterare.hanteraRad(tillstand, FELAKTIG_RAD, RADNUMMER);

        // THEN
        verify(tillstand).felaktigRad();
    }

    @Test
    void testLaggTillNyPerson() {
        // WHEN
        hanterare.hanteraRad(tillstand, PERSON_RAD, RADNUMMER);

        // THEN
        verify(validerare).kontrolleraPersonData(any());
        verify(tillstand).laggTillNyPerson(any());
    }

    @Test
    void testLaggTillTelefonnummer() {
        // WHEN
        hanterare.hanteraRad(tillstand, TELEFON_RAD, RADNUMMER);

        // THEN
        verify(validerare).kontrolleraTelefonnummerData(any());
        verify(tillstand).laggTillTelefonnummer(any());
    }

    @Test
    void testLaggTillAdress() {
        // WHEN
        hanterare.hanteraRad(tillstand, ADRESS_RAD, RADNUMMER);

        // THEN
        verify(validerare).kontrolleraAdressData(any());
        verify(tillstand).laggTillAdress(any());
    }

    @Test
    void testLaggTillNyFamiljemedlem() {
        // WHEN
        hanterare.hanteraRad(tillstand, FAMILJ_RAD, RADNUMMER);

        // THEN
        verify(validerare).kontrolleraFamiljemedlemData(any());
        verify(tillstand).laggTillNyFamiljemedlem(any());
    }
}
