package net.ancronik.cookbook.backend.web.advice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.ApiErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    /**
     * Method for handling all general exceptions with bad request response.
     *
     * @param exception  exception
     * @param request request
     * @return bad request with provided data
     */
    @ExceptionHandler({IllegalDataInRequestException.class})
    public ResponseEntity<ApiErrorResponse> badRequestHandler(Throwable exception, WebRequest request) {
        LOG.error("Bad request found {}", request, exception);

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                exception.getMessage(),
                exception instanceof IllegalDataInRequestException ? ((IllegalDataInRequestException) exception).getInvalidFieldsAsString() : exception.getMessage(),
                LocalDateTime.now(ZoneId.of("UTC"))
        ));
    }

    /**
     * Handler for exceptions thrown when requested data does not exist.
     *
     * @return not found
     */
    @ExceptionHandler({DataDoesNotExistException.class})
    public ResponseEntity<String> handleExceptionsWhenEntryDoesNotExistsInDatabase(Exception exception, WebRequest request) {
        LOG.error("Data not found {}", request, exception);
        return ResponseEntity.notFound().build();
    }

    /**
     * General handler
     *
     * @param exception exception
     * @param request   request
     * @return api error response
     */
    @ExceptionHandler({EmptyDataException.class, RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> internalServerExceptionsHandler(Exception exception, WebRequest request) {
        LOG.error("Unknown error occurred {}", request, exception);
        return ResponseEntity.internalServerError().body(
                new ApiErrorResponse(
                        exception.getMessage(),
                        exception.toString(),
                        LocalDateTime.now(ZoneId.of("UTC"))
                )
        );
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LOG.error("Argument not valid", ex);
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                ex.getMessage(),
                Arrays.toString(ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toArray()),
                LocalDateTime.now(ZoneId.of("UTC"))
        ));
    }
}