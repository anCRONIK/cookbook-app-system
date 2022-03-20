package net.ancronik.cookbook.backend.api.web.advice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.api.application.exceptions.CdnException;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.api.web.dto.ApiErrorResponse;
import net.ancronik.cookbook.backend.api.web.dto.ValidationFailedResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Advice which will handle all exceptions that are propagated from controllers.
 *
 * @author Nikola Presecki
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler for exceptions thrown when requested data does not exist.
     *
     * @return not found
     */
    @ExceptionHandler({DataDoesNotExistException.class})
    public ResponseEntity<String> handleExceptionsWhenEntryDoesNotExistsInDatabase(Exception e, WebRequest request) {
        LOG.error("Data not found {}", request, e);
        return ResponseEntity.notFound().build();
    }

    /**
     * Database exceptions handler
     *
     * @param e       exception
     * @param request request
     * @return api error response
     */
    @ExceptionHandler({EmptyDataException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> databaseExceptionsHandler(Exception e, WebRequest request) {
        LOG.error("Database error occurred {}", request, e);
        return ResponseEntity.internalServerError().body(
                new ApiErrorResponse(
                        "There is error with database", //TODO extract to messages file
                        e.toString(),
                        LocalDateTime.now(ZoneId.of("UTC"))
                )
        );
    }

    /**
     * General handler
     *
     * @param e       exception
     * @param request request
     * @return api error response
     */
    @ExceptionHandler({CdnException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> internalServerExceptionsHandler(Exception e, WebRequest request) {
        LOG.error("Unknown error occurred {}", request, e);
        return ResponseEntity.internalServerError().body(
                new ApiErrorResponse(
                        e.getMessage(),
                        e.toString(),
                        LocalDateTime.now(ZoneId.of("UTC"))
                )
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleValidationPathAndParamsError(ConstraintViolationException e, @NonNull WebRequest request) {
        LOG.error("Argument not valid {}", e.getMessage());

        Map<String, Object> errors = new HashMap<>();

        e.getConstraintViolations().forEach(constraintViolation -> errors.putIfAbsent(constraintViolation.getPropertyPath().toString().split("\\.")[1], constraintViolation.getMessage()));


        return ResponseEntity.badRequest().body(new ValidationFailedResponse(
                "Validation error", //FIXME messages.properties, do not use ex.getMessage because it is too descriptive
                "", //TODO can leave empty?
                LocalDateTime.now(ZoneId.of("UTC")),
                errors
        ));
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LOG.error("Argument not valid");

        Map<String, Object> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.putIfAbsent(fieldError.getField(), e.getBindingResult().getFieldErrors(fieldError.getField()).stream().map(FieldError::getDefaultMessage).collect(Collectors.toList())));

        LOG.error("{}", errors);

        return ResponseEntity.badRequest().body(new ValidationFailedResponse(
                "Validation error", //FIXME messages.properties, do not use ex.getMessage because it is too descriptive
                null,
                LocalDateTime.now(ZoneId.of("UTC")),
                errors
        ));
    }
}