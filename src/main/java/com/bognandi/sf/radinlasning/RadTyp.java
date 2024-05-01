package com.bognandi.sf.radinlasning;

import java.util.Arrays;
import java.util.Optional;

public enum RadTyp {
    PERSON("P"),
    TELEFONNUMMER("T"),
    ADRESS("A"),
    FAMILJEMEDLEM("F");

    String radTypKod;

    RadTyp(String radTypKod) {
        this.radTypKod = radTypKod;
    }

    public String getRadTypKod() {
        return radTypKod;
    }

    public static Optional<RadTyp> from(String radTypKod) {
        return Arrays.stream(RadTyp.values())
                .filter(enumValue -> enumValue.getRadTypKod().equals(radTypKod))
                .findFirst();
    }
}
