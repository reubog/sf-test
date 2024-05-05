package com.bognandi.sf;

import com.bognandi.sf.jaxb.PeopleJaxbAssembler;
import com.bognandi.sf.radinlasning.RadHanterarTillstand;
import com.bognandi.sf.radinlasning.RadHanterare;
import com.bognandi.sf.radinlasning.validering.RadHanterarValiderare;
import com.xmlsystem.People;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Konverterare {
    private static Logger LOG = LoggerFactory.getLogger(Konverterare.class);

    RadHanterarValiderare validerare = new RadHanterarValiderare();
    RadHanterare hanterare = new RadHanterare(validerare);
    PeopleJaxbAssembler jaxbAssembler = new PeopleJaxbAssembler();
    long radNummer;

    public KonverteringResultat konvertera(InputStream inData, OutputStream utData) {
        RadHanterarTillstand tillstand = new RadHanterarTillstand();

        lasInPersonerFranInData(inData, tillstand);
        LOG.atInfo().log("Inläsning av infil avslutad");

        People jaxbPeople = jaxbAssembler.assemblera(tillstand.getInlastaPersoner());
        LOG.atInfo().log("Assemblering avslutad");

        skrivTillUtStream(jaxbPeople, utData);
        LOG.atInfo().log("Skrivning till utfil avslutad");

        return new KonverteringResultat(
                radNummer,
                tillstand.getInlastaPersoner().size(),
                tillstand.getAntalFelaktigaPersoner(),
                tillstand.getAntalHanteradeRader(),
                tillstand.getAntalFelaktigaRader(),
                tillstand.getAntalOkandaRader(),
                tillstand.getAntalSkippadeRader()
        );
    }

    private void lasInPersonerFranInData(InputStream inData, RadHanterarTillstand tillstand) {
        BufferedReader reader;
        radNummer = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(inData));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                hanterare.hanteraRad(tillstand, line, ++radNummer);
            }

        } catch (IOException e) {
            throw new KonverterareException("Det gick inte att läsa infilen vid rad %d".formatted(radNummer), e);
        }
    }

    private void skrivTillUtStream(People jaxbPeople, OutputStream utData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(People.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(jaxbPeople, utData);
        } catch (JAXBException e) {
            throw new KonverterareException("Det gick inte att skriva utfilen", e);
        }
    }
}
