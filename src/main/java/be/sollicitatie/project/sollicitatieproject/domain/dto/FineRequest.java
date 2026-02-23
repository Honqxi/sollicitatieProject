package be.sollicitatie.project.sollicitatieproject.domain.dto;

import be.sollicitatie.project.sollicitatieproject.domain.FineStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FineRequest {

    @Min(value = 0, message = "Amount must be positive")
    private int amount;

    @NotBlank(message = "City can't be empty")
    private String city;

    @NotNull(message = "Status can't be empty")
    private FineStatus status;
}