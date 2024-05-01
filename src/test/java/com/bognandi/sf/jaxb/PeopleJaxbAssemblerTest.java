package com.bognandi.sf.jaxb;

import com.bognandi.sf.model.Adress;
import com.bognandi.sf.model.Familjemedlem;
import com.bognandi.sf.model.Person;
import com.bognandi.sf.model.Telefonuppgifter;
import com.xmlsystem.People;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleJaxbAssemblerTest {
    private static String FORNAMN = "elon";
    private static String EFTERNAMN = "musk";
    private static String NAMN = "elvis presley";
    private static int FODELSEAR = 1234;
    private static String MOBILNUMMER = "08-123456";
    private static String FASTNATSNUMMER = "070-1234567";
    private static String GATA = "Gatan 1";
    private static String STAD = "Staden";
    private static Integer POSTNUMMER = 12345;
    private static String GATA2 = "Gatan 2";
    private static String STAD2 = "Staaaaden";
    private static Integer POSTNUMMER2 = 54321;

    private PeopleJaxbAssembler assembler;

    @BeforeEach
    void setup() {
        assembler = new PeopleJaxbAssembler();
    }

    @Test
    void testAssemblerPerson() {
        // GIVEN
        Familjemedlem familjemedlem = new Familjemedlem(NAMN, FODELSEAR);
        familjemedlem.setAdress(new Adress(GATA2,STAD2,POSTNUMMER2));
        Person person = new Person(FORNAMN, EFTERNAMN);
        person.setAdress(new Adress(GATA,STAD,POSTNUMMER));
        person.setTelefonuppgifter(new Telefonuppgifter(MOBILNUMMER, FASTNATSNUMMER));
        person.addFamiljemedlem(familjemedlem);

        // WHEN
        People jaxbPeople = assembler.assemblera(Arrays.asList(person));

        // THEN
        assertEquals(1, jaxbPeople.getPerson().size());
        com.xmlsystem.Person jaxbPerson = jaxbPeople.getPerson().getFirst();
        assertEquals(FORNAMN, jaxbPerson.getFirstname());
        assertEquals(EFTERNAMN, jaxbPerson.getLastname());
        assertEquals(GATA, jaxbPerson.getAddress().getStreet());
        assertEquals(STAD, jaxbPerson.getAddress().getCity());
        assertEquals(POSTNUMMER, Integer.parseInt(jaxbPerson.getAddress().getZip()));
        assertEquals(MOBILNUMMER, jaxbPerson.getPhone().getMobile());
        assertEquals(FASTNATSNUMMER, jaxbPerson.getPhone().getLandline());
        assertEquals(NAMN, jaxbPerson.getFamily().getFirst().getName());
        assertEquals(FODELSEAR, jaxbPerson.getFamily().getFirst().getBorn());
        assertEquals(GATA2, jaxbPerson.getFamily().getFirst().getAddress().getStreet());
        assertEquals(STAD2, jaxbPerson.getFamily().getFirst().getAddress().getCity());
        assertEquals(POSTNUMMER2, Integer.parseInt(jaxbPerson.getFamily().getFirst().getAddress().getZip()));
    }

}
