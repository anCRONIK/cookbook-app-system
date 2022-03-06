package net.ancronik.cookbook.backend.web.advice;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExist;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.web.dto.ApiErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

/**
 * Advice which will handle all exceptions that are propagated from controllers.
 *
 * @author Nikola Presecki
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Method for handling all general exceptions with bad request response.
     *
     * @param ex         exception
     * @param webRequest request
     * @return bad request with provided data
     */
    @ExceptionHandler({EmptyDataException.class, RuntimeException.class})
    public ResponseEntity<ApiErrorResponse> badRequestHandler(Throwable ex, WebRequest webRequest) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                ex.getMessage(),
                ex.getMessage(), //FIXME
                LocalDateTime.now(ZoneId.of("UTC"))
        ));
    }

    /**
     * Handler for exceptions thrown when requested data does not exist.
     *
     * @return not found
     */
    @ExceptionHandler({DataDoesNotExist.class})
    public ResponseEntity<String> handleExceptionsWhenEntryDoesNotExistsInDatabase() {
        return ResponseEntity.notFound().build();
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                Arrays.toString(ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toArray()),
                "desc", //FIXME
                LocalDateTime.now(ZoneId.of("UTC"))
        ));
    }

    //TODO generic handler????
}