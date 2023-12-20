package academy.devdojo.springboot2.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MovieDTO {
    private Long id;

    @NotEmpty(message = "The movie name cannot be empty")
    private String name;
}
