package be.sollicitatie.project.sollicitatieproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    private String city;

    @Enumerated(EnumType.STRING)
    private FineStatus status;

    @ManyToOne
    private Person person;
}
