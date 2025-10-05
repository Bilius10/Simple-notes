package api.example.SimpleNotes.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(ServiceException ex,
                                                                 HttpServletRequest request) {
        if (ex.getStatus().is5xxServerError()) {
            log.error("Excecao base: {} - Caminho: {}", ex.getMessage(), request.getRequestURI(), ex);
        } else {
            log.warn("Excecao base: {} - Caminho: {} - Status: {}",
                    ex.getMessage(), request.getRequestURI(), ex.getStatus().value());
        }

        var response = ExceptionResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();


        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        var response = ExceptionResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(ex.getStatusCode().value())
                .message("Informações invalidas nos seguintes campos")
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
