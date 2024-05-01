package com.bognandi.sf.jaxb;

import com.bognandi.sf.model.Adress;
import com.bognandi.sf.model.Familjemedlem;
import com.bognandi.sf.model.Person;
import com.bognandi.sf.model.Telefonuppgifter;
import com.xmlsystem.ObjectFactory;
import com.xmlsystem.People;

import java.util.Collection;

import static java.util.Objects.isNull;

public class PeopleJaxbAssembler {

    private ObjectFactory factory = new ObjectFactory();

    public People assemblera(Collection<Person> personer) {
        People people = factory.createPeople();
        people.getPerson().addAll(personer.stream()
                .map(person -> skapaJaxbPerson(person))
                .toList());
        return people;
    }

    private com.xmlsystem.Person skapaJaxbPerson(Person person) {
        com.xmlsystem.Person jaxbPerson = factory.createPerson();
        jaxbPerson.setFirstname(person.getFornamn());
        jaxbPerson.setLastname(person.getEfternamn());
        jaxbPerson.setAddress(skapaJaxbAdress(person.getAdress()));
        jaxbPerson.setPhone(skapaJaxbTelefon(person.getTelefon()));
        jaxbPerson.getFamily().addAll(person.getFamiljemedlemmar().stream()
                .map(familjemedlem -> skapaJaxbFamiljemedlem(familjemedlem)).toList());
        return jaxbPerson;
    }

    private com.xmlsystem.Address skapaJaxbAdress(Adress adress) {
        if (isNull(adress)) {
            return null;
        }
        com.xmlsystem.Address jaxbAdress = factory.createAddress();
        jaxbAdress.setStreet(adress.getGata());
        jaxbAdress.setCity(adress.getStad());
        jaxbAdress.setZip(isNull(adress.getPostnummer()) ? "" : String.valueOf(adress.getPostnummer()));
        return jaxbAdress;
    }

    private com.xmlsystem.Phone skapaJaxbTelefon(Telefonuppgifter telefonuppgifter) {
        if (isNull(telefonuppgifter)) {
            return null;
        }
        com.xmlsystem.Phone jaxbTelefon = factory.createPhone();
        jaxbTelefon.setMobile(telefonuppgifter.getMobilnummer());
        jaxbTelefon.setLandline(telefonuppgifter.getFastnatsnummer());
        return jaxbTelefon;
    }

    private com.xmlsystem.Family skapaJaxbFamiljemedlem(Familjemedlem familjemedlem) {
        com.xmlsystem.Family jaxbFamiljemedlem = factory.createFamily();
        jaxbFamiljemedlem.setName(familjemedlem.getNamn());
        jaxbFamiljemedlem.setBorn(familjemedlem.getFodelsear());
        jaxbFamiljemedlem.setAddress(skapaJaxbAdress(familjemedlem.getAdress()));
        jaxbFamiljemedlem.setPhone(skapaJaxbTelefon(familjemedlem.getTelefonuppgifter()));
        return jaxbFamiljemedlem;
    }
}
