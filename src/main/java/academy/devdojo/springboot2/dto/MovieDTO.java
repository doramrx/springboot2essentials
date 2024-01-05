package academy.devdojo.springboot2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long id;

    @NotEmpty(message = "The movie name cannot be empty")
    @Schema(description = "This is the Movie's name", example = "Butterfly effect")
    private String name;
}
