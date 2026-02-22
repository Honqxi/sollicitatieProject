package be.sollicitatie.project.sollicitatieproject.repository;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByPersonId(Long personId);
}
