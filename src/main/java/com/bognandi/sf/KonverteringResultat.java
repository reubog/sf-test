package com.bognandi.sf;

record KonverteringResultat(
        long antalRaderTotalt,
        long antalKonverteradePersoner,
        long antalFelaktigaPersoner,
        long antalHanteradeRader,
        long antalOkandaRader,
        long antalSkippadeRader,
        long antalFelaktigaRader
) {
}
