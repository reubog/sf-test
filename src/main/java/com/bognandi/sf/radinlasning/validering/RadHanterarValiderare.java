package com.bognandi.sf.radinlasning.validering;

import com.bognandi.sf.radinlasning.HanteringObjektTyp;
import com.bognandi.sf.radinlasning.RadTyp;

import java.util.Objects;

import static java.util.Objects.isNull;

public class RadHanterarValiderare {

    private static String REGEX_POSTNUMMER = "\\d{5}";
    private static String REGEX_TELEFONNUMMER = "0\\d{1,3}-\\d{5,8}";   //ungefärlig
    private static String REGEX_FODELSEAR = "\\d{4}";

    public void kontrolleraRadTypGiltig(HanteringObjektTyp hanteringObjektTyp, RadTyp radTyp) {
        if (isNull(hanteringObjektTyp) && RadTyp.PERSON.equals(radTyp)) {
            return;     // initialt tillstånd
        }

        Objects.requireNonNull(hanteringObjektTyp, "Hanterat objekt måste anges");
        Objects.requireNonNull(radTyp, "RadTyp måste anges");
    }

    public void kontrolleraPersonData(String[] radData) {
        if (radData.length != 3 || radData[1].isBlank() || radData[2].isBlank()) {
            throw new ValideringException("Persondata kräver följande radformat: 'P|förnamn|efternamn'");
        }
    }

    public void kontrolleraTelefonnummerData(String[] radData) {
        if (radData.length != 3 || radData[1].isBlank() || radData[2].isBlank() || !radData[1].matches(REGEX_TELEFONNUMMER) || !radData[2].matches(REGEX_TELEFONNUMMER)) {
            throw new ValideringException("Telefonuppgifter kräver följande radformat: 'T|mobilnummer|fastnätsnummer'");
        }
    }

    public void kontrolleraAdressData(String[] radData) {
        boolean fulltVillkorOk = radData.length == 4 && !radData[1].isBlank() && !radData[2].isBlank() && !radData[3].isBlank() && radData[3].matches(REGEX_POSTNUMMER);
        boolean halvtVillkorOk = radData.length == 3 && !radData[1].isBlank() && !radData[2].isBlank();
        if (!(fulltVillkorOk || halvtVillkorOk)) {
            throw new ValideringException("Adress kräver följande radformat: 'A|gata|stad|postnummer'");
        }
    }

    public void kontrolleraFamiljemedlemData(String[] radData) {
        if (radData.length != 3 || radData[1].isBlank() || radData[2].isBlank() || !radData[2].matches(REGEX_FODELSEAR)) {
            throw new ValideringException("Familjemedlem kräver följande radformat: 'F|namn|födelseår'");
        }
    }
}
