package be.sollicitatie.project.sollicitatieproject.controller;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import be.sollicitatie.project.sollicitatieproject.Service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fine> create(@RequestBody Fine fine) {
        Fine created = fineService.create(fine);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Fine>> findAll() {
        return ResponseEntity.ok(fineService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Fine> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fine> update(@PathVariable Long id, @RequestBody Fine fine) {
        return ResponseEntity.ok(fineService.update(id, fine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/person/{personId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Fine>> findByPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(fineService.findByPerson(personId));
    }

    @GetMapping("/total-per-city")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Integer>> totalAmountPerCity() {
        return ResponseEntity.ok(fineService.totalAmountPerCity());
    }
}
