package be.sollicitatie.project.sollicitatieproject.Service;

import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.domain.dto.PersonRequest;
import be.sollicitatie.project.sollicitatieproject.repository.IPersonRepository;
import be.sollicitatie.project.sollicitatieproject.Service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {
    @Mock
    private IPersonRepository personRepository;
    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFirstName("Joakim");
        person.setLastName("Libioulle");
        person.setEmail("libioullejoakim@gmail.com");
    }

    @Test
    void shouldReturnAllPersons() {
        Person person2 = new Person();
        person2.setId(1L);
        person2.setEmail("libioullejoakim2@gmail.com");
        person2.setFirstName("Joakim2");
        person2.setLastName("Libioulle2");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        when(personRepository.findAll()).thenReturn(persons);

        List<Person> result = personService.findAll();

        assertEquals(2, result.size());
        verify(personRepository).findAll();
    }

    @Test
    void shouldReturnPersonWhenFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Person result = personService.findById(1L);

        assertEquals("Joakim", result.getFirstName());
        assertEquals("Libioulle", result.getLastName());
        assertEquals("libioullejoakim@gmail.com", result.getEmail());
        assertEquals(1L, result.getId());

    }

    @Test
    void shouldThrowExceptionWhenPersonNotFound(){
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> personService.findById(1L));
    }

    @Test
    void shouldCreatePerson() {
        // DTO invullen
        PersonRequest personRequest = new PersonRequest();
        personRequest.setEmail("libioullejoakim@gmail.com");
        personRequest.setFirstName("Joakim");
        personRequest.setLastName("Libioulle");

        // Verwachte entity die repository zal teruggeven
        Person savedPerson = Person.builder()
                .id(1L)
                .email(personRequest.getEmail())
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .build();

        // Mock repository
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Service aanroepen
        Person result = personService.create(personRequest);

        // Asserties op het resultaat
        assertEquals(personRequest.getEmail(), result.getEmail());
        assertEquals(personRequest.getFirstName(), result.getFirstName());
        assertEquals(personRequest.getLastName(), result.getLastName());
        assertNotNull(result.getId()); // Check dat ID is gegenereerd
    }

    @Test
    void shouldDeletePersonById(){
        personService.deleteById(1L);

        verify(personRepository).deleteById(1L);
    }

    @Test
    void shouldUpdatePerson() {
        Person existingPerson = new Person();
        existingPerson.setId(1L);
        existingPerson.setEmail("libioullejoakim@gmail.com");
        existingPerson.setFirstName("Joakim");
        existingPerson.setLastName("Libioulle");

        Person changedPerson = new Person();
        changedPerson.setFirstName("John");
        changedPerson.setLastName("Doe");
        changedPerson.setEmail("libioullejoakim@gmail.com");

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(existingPerson));

        when(personRepository.save(any(Person.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Person result = personService.update(1L, changedPerson);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("libioullejoakim@gmail.com", result.getEmail());

        verify(personRepository).findById(1L);
        verify(personRepository).save(existingPerson);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> personService.update(1L, person));
    }
}
