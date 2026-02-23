package be.sollicitatie.project.sollicitatieproject.Service.impl;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.domain.dto.FineRequest;
import be.sollicitatie.project.sollicitatieproject.repository.IFineRepository;
import be.sollicitatie.project.sollicitatieproject.Service.FineService;
import be.sollicitatie.project.sollicitatieproject.repository.IPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final IFineRepository fineRepository;
    private final IPersonRepository personRepository;

    @Override
    public Fine create(Long personId, FineRequest fine) {
        Fine newFine = new Fine();
        newFine.setAmount(fine.getAmount());
        newFine.setCity(fine.getCity());
        newFine.setStatus(fine.getStatus());

        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person with id " + personId + " not found"));
        newFine.setPerson(person);
        return fineRepository.save(newFine);
    }

    @Override
    public Fine update(Long id, Fine fine) {
        Fine existing = fineRepository.findById(id).orElseThrow(() -> new RuntimeException("Fine not found with id " + id));

        existing.setAmount(fine.getAmount());
        existing.setCity(fine.getCity());
        existing.setStatus(fine.getStatus());
        existing.setPerson(fine.getPerson());
        return fineRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        fineRepository.deleteById(id);
    }

    @Override
    public Fine findById(Long id) {
        return fineRepository.findById(id).orElseThrow(() -> new RuntimeException("Fine not found with id " + id));
    }

    @Override
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }

    @Override
    public List<Fine> findByPerson(Long personId) {
        return fineRepository.findByPersonId(personId);
    }

    @Override
    public Map<String, Integer> totalAmountPerCity() {
        Map<String, Integer> totalFineAmountPerCity = new HashMap<>();

        for (Fine fine : fineRepository.findAll()) {
            totalFineAmountPerCity.put(fine.getCity(), totalFineAmountPerCity.getOrDefault(fine.getCity(), 0) + fine.getAmount()
            );
        }

        return totalFineAmountPerCity;
    }
}
