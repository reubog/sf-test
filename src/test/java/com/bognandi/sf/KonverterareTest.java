package com.bognandi.sf;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KonverterareTest {

    @Test
    void testKonverareHappyCase() throws IOException, ParserConfigurationException, TransformerException, JAXBException {
        // GIVEN
        byte[] indata = this.getClass().getResourceAsStream("/infil.txt").readAllBytes();
        InputStream inputStream = new ByteArrayInputStream(indata);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);

        // WHEN
        Konverterare.konvertera(inputStream, outputStream);

        // THEN
        outputStream.flush();
        byte[] utData = outputStream.toByteArray();
        byte[] expectedData = this.getClass().getResourceAsStream("/utfil.xml").readAllBytes();

        Assertions.assertArrayEquals(expectedData, utData);

        inputStream.close();
        outputStream.close();
    }


}
