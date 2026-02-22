package be.sollicitatie.project.sollicitatieproject.controller;

import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.Service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> create(@RequestBody Person person){
        Person createdPerson = personService.create(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Person>> findAll(){
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Person> findById(@PathVariable Long id){
        return ResponseEntity.ok(personService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> update(
            @PathVariable Long id,
            @RequestBody Person person) {

        return ResponseEntity.ok(personService.update(id, person));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
