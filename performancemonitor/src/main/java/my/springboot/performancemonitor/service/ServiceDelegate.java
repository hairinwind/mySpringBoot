package my.springboot.performancemonitor.service;

import my.springboot.performancemonitor.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDelegate {

    @Autowired
    private PersonService personService;

    public Person getPerson() {
        return personService.getPerson();
    }
}
