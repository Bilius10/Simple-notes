package api.example.SimpleNotes.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
