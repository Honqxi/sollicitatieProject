package be.sollicitatie.project.sollicitatieproject.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonRequest {
    @NotNull(message = "Firstname is required")
    private String firstName;
    @NotNull(message = "Lastname is required")
    private String lastName;
    @NotNull(message = "email is required")
    private String email;
}
