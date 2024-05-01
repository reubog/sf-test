package com.bognandi.sf.radinlasning.validering;

import com.bognandi.sf.radinlasning.HanteringObjektTyp;
import com.bognandi.sf.radinlasning.RadTyp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RadHanderareValiderareTest {

    private RadHanterarValiderare validerare = new RadHanterarValiderare();

    static Stream<Arguments> giltigaRadTyperSource() {
        return Stream.of(
                Arguments.of(null, RadTyp.PERSON),
                Arguments.of(HanteringObjektTyp.PERSON, RadTyp.PERSON),
                Arguments.of(HanteringObjektTyp.PERSON, RadTyp.TELEFONNUMMER),
                Arguments.of(HanteringObjektTyp.PERSON, RadTyp.ADRESS),
                Arguments.of(HanteringObjektTyp.PERSON, RadTyp.FAMILJEMEDLEM),
                Arguments.of(HanteringObjektTyp.FAMILJEMEDLEM, RadTyp.TELEFONNUMMER),
                Arguments.of(HanteringObjektTyp.FAMILJEMEDLEM, RadTyp.ADRESS),
                Arguments.of(HanteringObjektTyp.FAMILJEMEDLEM, RadTyp.FAMILJEMEDLEM)
        );
    }

    @ParameterizedTest
    @MethodSource("giltigaRadTyperSource")
    void testaKontrolleraRadTypOk(HanteringObjektTyp hanteringObjektTyp, RadTyp radTyp) {
        validerare.kontrolleraRadTypGiltig(hanteringObjektTyp, radTyp);     // inget exception
    }

    @Test
    void testaKontrolleraRadTypNullGerException() {
        assertThrows(NullPointerException.class, () -> validerare.kontrolleraRadTypGiltig(HanteringObjektTyp.PERSON, null));
    }

    @ParameterizedTest
    @EnumSource(value = RadTyp.class, mode = EnumSource.Mode.EXCLUDE, names = "PERSON")
    void testaKontrolleraHanteringObjektTypNullGerException(RadTyp radTyp) {
        assertThrows(NullPointerException.class, () -> validerare.kontrolleraRadTypGiltig(null, radTyp));
    }

    @ParameterizedTest
    @ValueSource(strings = {"P|fornamn|efternamn", "P|for-namn|efter-namn"})
    void testaKontrolleraPersonDataOk(String str) {
        validerare.kontrolleraPersonData(str.split("\\|"));     // inget exception
    }

    @ParameterizedTest
    @ValueSource(strings = {"P", "P||", "P||efternamn", "P|fornamn|"})
    void testaKontrolleraPersonDataFel(String str) {
        assertThrows(ValideringException.class, () -> validerare.kontrolleraPersonData(str.split("\\|")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"T|08-123456|070-1234567", "P|0123-12345|012-123456"})
    void testaKontrolleraTelefonnummerDataOk(String str) {
        validerare.kontrolleraPersonData(str.split("\\|"));     // inget exception
    }

    @ParameterizedTest
    @ValueSource(strings = {"T", "T|08-123456", "T|0-123456|1234-12345", "T|0123-456789123|012-123456"})
    void testaKontrolleraTelefonnummerDataFel(String str) {
        assertThrows(ValideringException.class, () -> validerare.kontrolleraTelefonnummerData(str.split("\\|")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A|Gatan 1|Staden", "A|Gatan 1|Staden|12345"})
    void testaKontrolleraAdressDataOk(String str) {
        validerare.kontrolleraAdressData(str.split("\\|"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "A|gata", "A|gata 12|", "A||Staaden", "A||", "A|Gatan 1|Staaden|1234", "A|Gatan 1|Staaden|123456"})
    void testaKontrolleraAdressDataFel(String str) {
        assertThrows(ValideringException.class, () -> validerare.kontrolleraAdressData(str.split("\\|")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"F|namn|2001", "F|annat namn|1234"})
    void testaKontrolleraFamiljemedlemDataOk(String str) {
        validerare.kontrolleraAdressData(str.split("\\|"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "F||", "F|namn|", "F|namn|123", "F|namn|12345"})
    void testaKontrolleraFamiljemedlemDataFel(String str) {
        assertThrows(ValideringException.class, () -> validerare.kontrolleraFamiljemedlemData(str.split("\\|")));
    }

}
