package be.sollicitatie.project.sollicitatieproject.Service;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import be.sollicitatie.project.sollicitatieproject.domain.FineStatus;
import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.repository.IFineRepository;
import be.sollicitatie.project.sollicitatieproject.Service.impl.FineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FineServiceImplTest {
    @Mock
    private IFineRepository fineRepository;
    @InjectMocks
    private FineServiceImpl fineService;

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
    void shouldReturnAllFines() {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(1L);
        fine.setCity("Sint-Truiden");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        Fine fine2 = new Fine();
        fine2.setPerson(person);
        fine2.setId(2L);
        fine2.setCity("Sint-Truiden");
        fine2.setStatus(FineStatus.PAID);
        fine2.setAmount(99);

        List<Fine> fines = new ArrayList<>();
        fines.add(fine);
        fines.add(fine2);

        when(fineRepository.findAll()).thenReturn(fines);

        List<Fine> result = fineService.findAll();

        assertEquals(2, result.size());
        verify(fineRepository).findAll();
    }

    @Test
    void shouldReturnFineWhenFound() {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(1L);
        fine.setCity("Sint-Truiden");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        Fine result = fineService.findById(1L);

        assertEquals("Sint-Truiden", result.getCity());
        assertEquals(99, result.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenFineNotFound() {
        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fineService.findById(1L));
    }

    @Test
    void shouldCreateFine() {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(null);
        fine.setCity("Sint-Truiden");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        Fine savedFine = new Fine();
        savedFine.setPerson(person);
        savedFine.setId(null);
        savedFine.setCity("Sint-Truiden");
        savedFine.setStatus(FineStatus.OPEN);
        savedFine.setAmount(110);

        when(fineRepository.save(fine)).thenReturn(savedFine);

        Fine result = fineService.create(fine);

        assertEquals(result, savedFine);
        verify(fineRepository).save(fine);
    }

    @Test
    void shouldUpdateFine() {
        Fine existingFine = new Fine();
        existingFine.setPerson(person);
        existingFine.setId(1L);
        existingFine.setCity("Sint-Truiden");
        existingFine.setStatus(FineStatus.OPEN);
        existingFine.setAmount(99);

        Fine updatedFine = new Fine();
        updatedFine.setPerson(person);
        updatedFine.setId(null);
        updatedFine.setCity("Sint-Truiden");
        updatedFine.setStatus(FineStatus.OPEN);
        updatedFine.setAmount(110);

        when(fineRepository.findById(1L))
                .thenReturn(Optional.of(existingFine));

        when(fineRepository.save(any(Fine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fine result = fineService.update(1L, updatedFine);

        assertEquals(1L, result.getId());
        assertEquals("Sint-Truiden", result.getCity());
        assertEquals(110, result.getAmount());

        verify(fineRepository).findById(1L);
        verify(fineRepository).save(existingFine);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingFine() {
        Fine updatedFine = new Fine();
        updatedFine.setPerson(person);
        updatedFine.setId(null);
        updatedFine.setCity("Sint-Truiden");
        updatedFine.setStatus(FineStatus.OPEN);
        updatedFine.setAmount(99);

        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fineService.update(1L, updatedFine));
    }

    @Test
    void shouldDeleteFineById() {
        fineService.deleteById(1L);

        verify(fineRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTotalAmountPerCity() {
        Fine fine1 = new Fine();
        fine1.setPerson(person);
        fine1.setId(1L);
        fine1.setCity("Sint-Truiden");
        fine1.setStatus(FineStatus.OPEN);
        fine1.setAmount(99);

        Fine fine2 = new Fine();
        fine2.setPerson(person);
        fine2.setId(2L);
        fine2.setCity("Sint-Truiden");
        fine2.setStatus(FineStatus.OPEN);
        fine2.setAmount(99);

        Fine fine3 = new Fine();
        fine3.setPerson(person);
        fine3.setId(3L);
        fine3.setCity("Landen");
        fine3.setStatus(FineStatus.PAID);
        fine3.setAmount(99);

        when(fineRepository.findAll()).thenReturn(List.of(fine1, fine2, fine3));

        Map<String, Integer> result = fineService.totalAmountPerCity();

        assertEquals(2, result.size());
        assertEquals(99, result.get("Landen"));
        assertEquals(198, result.get("Sint-Truiden"));

        verify(fineRepository).findAll();
    }

    @Test
    void shouldReturnFineById() {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(1L);
        fine.setCity("Sint-Truiden");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        Fine result = fineService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals(99, result.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenFineNotFoundById() {
        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fineService.findById(1L));
    }

    @Test
    void shouldReturnFinesByPerson() {
        Fine fine1 = new Fine();
        fine1.setPerson(person);
        fine1.setId(1L);
        fine1.setCity("Sint-Truiden");
        fine1.setStatus(FineStatus.OPEN);
        fine1.setAmount(99);

        Fine fine2 = new Fine();
        fine2.setPerson(person);
        fine2.setId(2L);
        fine2.setCity("Sint-Truiden");
        fine2.setStatus(FineStatus.OPEN);
        fine2.setAmount(99);

        when(fineRepository.findByPersonId(person.getId())).thenReturn(List.of(fine1, fine2));

        List<Fine> result = fineService.findByPerson(person.getId());

        assertEquals(2, result.size());
        verify(fineRepository).findByPersonId(person.getId());
    }

}
