package be.sollicitatie.project.sollicitatieproject.Service.impl;

import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.repository.IPersonRepository;
import be.sollicitatie.project.sollicitatieproject.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final IPersonRepository personRepository;

    @Override
    public Person create(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person update(long id, Person person) {
        Person existing = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found with id " + id));

        existing.setFirstName(person.getFirstName());
        existing.setLastName(person.getLastName());
        existing.setEmail(person.getEmail());

        return personRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found with id " + id));
    }
}
