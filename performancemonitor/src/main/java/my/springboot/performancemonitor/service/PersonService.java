package my.springboot.performancemonitor.service;

import my.springboot.performancemonitor.domain.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PersonService {

    public Person getPerson() {
        return Person.builder().firstName("John")
                .lastName("Terry")
                .dateOfBirth(LocalDate.of(1990, 1,1 ))
                .build();
    }
}
