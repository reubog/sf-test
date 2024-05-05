package com.bognandi.sf.radinlasning;

import com.bognandi.sf.radinlasning.validering.RadHanterarValiderare;
import com.bognandi.sf.radinlasning.validering.ValideringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Objects;
import java.util.Optional;

public class RadHanterare {
    private static Logger LOG = LoggerFactory.getLogger(RadHanterare.class);

    public static String MDC_RADNUMMER = "radnummer"; // används i logback

    private static String KOLUMN_DELARE = "\\|";
    private RadHanterarValiderare validerare;

    public RadHanterare(RadHanterarValiderare validerare) {
        this.validerare = validerare;
    }

    public void hanteraRad(RadHanterarTillstand tillstand, String rad, long radNummer) {
        MDC.put(MDC_RADNUMMER, String.valueOf(radNummer));
        Objects.requireNonNull(tillstand, "Ett tillståndsobjekt krävs för exekvering");

        if (isRadSomHarInnehall(rad)) {
            try {
                String[] radData = rad.split(KOLUMN_DELARE);
                Optional<RadTyp> radTypOptional = RadTyp.from(radData[0]);
                if (radTypOptional.isEmpty()) {
                    tillstand.okandRad();
                    LOG.atWarn().log("Rad med okänt innehåll: %s".formatted(rad));
                } else {
                    hanteraRadMedInnehall(radTypOptional.get(), tillstand, radData);
                    tillstand.hanteradRad();
                }
            } catch (IllegalArgumentException | ValideringException e) {
                tillstand.felaktigRad();
                LOG.atWarn().log("Felaktig rad '%s': %s".formatted(e.getMessage(), rad));
            }
        } else {
            tillstand.skippadRad();
            LOG.atInfo().log("Raden skippas eftersom den saknar innehåll");
        }
    }

    private boolean isRadSomHarInnehall(String rad) {
        return !(Objects.isNull(rad) || rad.isBlank());
    }

    private void hanteraRadMedInnehall(RadTyp radTyp, RadHanterarTillstand tillstand, String[] radData) {
        switch (radTyp) {
            case PERSON -> hanteraPerson(tillstand, radData);
            case TELEFONNUMMER -> hanteraTelefonnummer(tillstand, radData);
            case ADRESS -> hanteraAdress(tillstand, radData);
            case FAMILJEMEDLEM -> hanteraFamiljemedlem(tillstand, radData);
        }
    }

    private void hanteraPerson(RadHanterarTillstand tillstand, String[] radData) {
        try {
            validerare.kontrolleraPersonData(radData);
            tillstand.laggTillNyPerson(radData);
        } catch (ValideringException e) {
            tillstand.felaktigDataForPerson(radData);
            throw e;
        }
    }

    private void hanteraTelefonnummer(RadHanterarTillstand tillstand, String[] radData) {
        try {
            validerare.kontrolleraTelefonnummerData(radData);
            tillstand.laggTillTelefonnummer(radData);
        } catch (ValideringException e) {
            tillstand.felaktigDataForPerson(radData);
            throw e;
        }
    }

    private void hanteraAdress(RadHanterarTillstand tillstand, String[] radData) {
        try {
            validerare.kontrolleraAdressData(radData);
            tillstand.laggTillAdress(radData);
        } catch (ValideringException e) {
            tillstand.felaktigDataForPerson(radData);
            throw e;
        }
    }

    private void hanteraFamiljemedlem(RadHanterarTillstand tillstand, String[] radData) {
        try {
            validerare.kontrolleraFamiljemedlemData(radData);
            tillstand.laggTillNyFamiljemedlem(radData);
        } catch (ValideringException e) {
            tillstand.felaktigDataForPerson(radData);
            throw e;
        }
    }

}
