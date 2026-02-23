package be.sollicitatie.project.sollicitatieproject.Service;

import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.domain.dto.PersonRequest;

import java.util.List;

public interface PersonService {
    Person create(PersonRequest person);
    Person update(long id, Person person);
    void deleteById(Long id);
    List<Person> findAll();
    Person findById(Long id);
}
