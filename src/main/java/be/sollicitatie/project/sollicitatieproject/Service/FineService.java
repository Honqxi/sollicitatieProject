package be.sollicitatie.project.sollicitatieproject.Service;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import be.sollicitatie.project.sollicitatieproject.domain.dto.FineRequest;

import java.util.List;
import java.util.Map;

public interface FineService {
    Fine create(Long personId, FineRequest fine);
    Fine update(Long id, Fine fine);

    void deleteById(Long id);

    Fine findById(Long id);
    List<Fine> findAll();
    List<Fine> findByPerson(Long personId);

    Map<String, Integer> totalAmountPerCity();
}
