package com.bognandi.sf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.*;

@CommandLine.Command(
        name = "sf-test",
        mixinStandardHelpOptions = true
)
public class SFTestMain implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(SFTestMain.class);

    @CommandLine.Option(
            names = {"-i", "--infil"},
            required = true,
            description = "Infil med rad-format som ska konverteras"
    )
    private String inFil;

    @CommandLine.Option(
            names = {"-u", "--utfil"},
            required = true,
            description = "Utfil med xml-format som har konverterats"
    )
    private String utFil;

    @Override
    public void run() {
        Konverterare konverterare = new Konverterare();

        try (
                InputStream inputStream = new FileInputStream(inFil);
                OutputStream outputStream = new FileOutputStream(utFil);
        ) {
            LOG.atInfo().log("Startar konvertering");
            KonverteringResultat resultat = konverterare.konvertera(inputStream, outputStream);
            LOG.atInfo().log("Konvertering avslutad");

            LOG.atInfo().log("""
                    Konverteringsresultat:
                    Totalt antal rader: %d                    
                    Antal inlästa personer: %d
                    Antal felaktiga personer: %d
                    Hanterade Rader: %d
                    Felaktiga Rader: %d
                    Okända rader: %d
                    Tomma rader: %d
                    """.formatted(
                    resultat.antalRaderTotalt(),
                    resultat.antalKonverteradePersoner(),
                    resultat.antalFelaktigaPersoner(),
                    resultat.antalHanteradeRader(),
                    resultat.antalFelaktigaRader(),
                    resultat.antalOkandaRader(),
                    resultat.antalSkippadeRader()
            ));
        } catch (IOException e) {
            throw new RuntimeException("Konverteringen misslyckades", e);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SFTestMain()).execute(args);
        System.exit(exitCode);
    }
}
