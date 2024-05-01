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
import picocli.CommandLine;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;

@CommandLine.Command(
        name = "rader2xml",
        mixinStandardHelpOptions = true
)
public class Konverterare implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(Konverterare.class);

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
//        String inFil = "src/test/resources/infil.txt";
//        String utFil = "utfil.xml";
        try {
            LOG.atInfo().log("Startar konvertering");
            konvertera(new FileInputStream(inFil), new FileOutputStream(utFil));
            LOG.atInfo().log("Konvertering avslutad");
        } catch (JAXBException | ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, JAXBException {
        int exitCode = new CommandLine(new Konverterare()).execute(args);
        System.exit(exitCode);
    }

    public static void konvertera(InputStream inData, OutputStream utData) throws ParserConfigurationException, TransformerException, IOException, JAXBException {
        RadHanterarValiderare validerare = new RadHanterarValiderare();
        RadHanterare hanterare = new RadHanterare(validerare);
        RadHanterarTillstand tillstand = new RadHanterarTillstand();

        BufferedReader reader;
        long radNummer = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(inData));
            String line = reader.readLine();

            while (line != null) {
                hanterare.hanteraRad(tillstand, line, ++radNummer);

                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.atInfo().log("""
                Inläsning fullgjord, resultat:
                Totalt antal rader: %d
                Hanterade Rader: %d
                Felaktiga Rader: %d
                Okända rader: %d
                Tomma rader: %d
                Antal inlästa personer: %d
                """.formatted(
                radNummer,
                tillstand.getAntalHanteradeRader(),
                tillstand.getAntalFelaktigaRader(),
                tillstand.getAntalOkandaRader(),
                tillstand.getAntalSkippadeRader(),
                tillstand.getInlastaPersoner().size()
        ));

        OutputStream outputStream = new BufferedOutputStream(utData);
        PeopleJaxbAssembler assembler = new PeopleJaxbAssembler();
        People jaxbPeople = assembler.assemblera(tillstand.getInlastaPersoner());

        JAXBContext jaxbContext = JAXBContext.newInstance(People.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(jaxbPeople, outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
