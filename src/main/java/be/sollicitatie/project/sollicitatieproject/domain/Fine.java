package be.sollicitatie.project.sollicitatieproject.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "fine")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
