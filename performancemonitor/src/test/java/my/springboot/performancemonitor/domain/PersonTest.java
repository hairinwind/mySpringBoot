package my.springboot.performancemonitor.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testDataAnotation() {
        Person person = new Person();
        person.setLastName("test_lastName");
        assertEquals("test_lastName", person.getLastName());

        System.out.println("toString(): " + person.toString());
        assertNotNull(person.toString());

        System.out.println("hash(): " + person.hashCode());
        assertTrue(person.hashCode() != 0);

        Person person1 = new Person();
        person1.setLastName("test_lastName");
        assertEquals(person, person1);

        person1.setFirstName("test_firstName");
        assertNotEquals(person, person1);
    }

    @Test
    public void testBuilder() {
        Person person = Person.builder()
                .dateOfBirth(LocalDate.of(1900,1,1))
                .firstName("John")
                .lastName("Terry")
                .build();
        assertEquals("John", person.getFirstName());
        assertEquals("Terry", person.getLastName());
        assertEquals(LocalDate.of(1900,1,1), person.getDateOfBirth());
    }
}