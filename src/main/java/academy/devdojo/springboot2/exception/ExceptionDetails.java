package academy.devdojo.springboot2.exception;

import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;
}
