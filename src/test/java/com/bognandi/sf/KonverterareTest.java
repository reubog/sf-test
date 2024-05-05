package com.bognandi.sf;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class KonverterareTest {
    private static int BUF_SIZE = 2048;

    private Konverterare konverterare = new Konverterare();

    @Test
    void testKonverareHappyCase() throws IOException {
        verifieraKonverteringFranResource("/infil.txt", "/utfil.xml");
    }

    @Test
    void testKonverareFel() throws IOException {
        verifieraKonverteringFranResource("/infil-fel.txt", "/utfil-fel.xml");
    }

    @Test
    void testKonverareTomrader() throws IOException {
        verifieraKonverteringFranResource("/infil-tomrader.txt", "/utfil-tomrader.xml");
    }

    @Test
    void testKonverareFamilj() throws IOException {
        verifieraKonverteringFranResource("/infil-familj.txt", "/utfil-familj.xml");
    }

    @Test
    void testKonverareTelefonnummer() throws IOException {
        verifieraKonverteringFranResource("/infil-telefonnummer.txt", "/utfil-telefonnummer.xml");
    }

    @Test
    void testKonverarePerson() throws IOException {
        verifieraKonverteringFranResource("/infil-person.txt", "/utfil-person.xml");
    }

    @Test
    void testKonverareOkantInnehall() throws IOException {
        verifieraKonverteringFranResource("/infil-okand.txt", "/utfil-okand.xml");
    }

    @Test
    void testInlasningFel() throws IOException {
        try (
                InputStream inputStream = mock(InputStream.class);
                ByteArrayOutputStream outputStream = mock(ByteArrayOutputStream.class);
        ) {
            // GIVEN
            when(inputStream.read()).thenThrow(IOException.class);

            // WHEN / THEN
            assertThrows(KonverterareException.class, () -> konverterare.konvertera(inputStream, outputStream));
        }
    }

    @Test
    void testSkrivningFel() throws IOException {
        try (
                InputStream inputStream = this.getClass().getResourceAsStream("/infil.txt");
                ByteArrayOutputStream outputStream = mock(ByteArrayOutputStream.class);
        ) {
            // GIVEN
            doThrow(IOException.class).when(outputStream).write(any(),anyInt(), anyInt());

            // WHEN / THEN
            assertThrows(KonverterareException.class, () -> konverterare.konvertera(inputStream, outputStream));
        }
    }

    void verifieraKonverteringFranResource(String inFil, String expectedUtFil) throws IOException {
        try (
                InputStream inputStream = this.getClass().getResourceAsStream(inFil);
                InputStream expectedStream = this.getClass().getResourceAsStream(expectedUtFil);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUF_SIZE);
        ) {
            // WHEN
            konverterare.konvertera(inputStream, outputStream);

            // THEN
            assertEquals(
                    new String(expectedStream.readAllBytes(), StandardCharsets.UTF_8),
                    new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        }
    }
}
