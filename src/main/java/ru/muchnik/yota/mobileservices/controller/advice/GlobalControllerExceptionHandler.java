package ru.muchnik.yota.mobileservices.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.muchnik.yota.mobileservices.model.ErrorResponse;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;

/**
 * Centralized exception handing for all {@link org.springframework.web.bind.annotation.RequestMapping} via {@link ExceptionHandler} methods
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * In case of any unknown (non-business) exceptions
     * throwed in {@link org.springframework.web.bind.annotation.RestController} methods
     *
     * @param ex throwed excepion
     * @return business {@link ResponseEntity}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final Throwable ex) {
        log.error("Handling unknown (non-business) exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .cause(ex.getMessage())
                        .build());
    }

    /**
     * In case if request is made to unsupported url
     *
     * @param ex      throwed exception
     * @param headers headers
     * @param status  http status
     * @param request web request
     * @return business {@link ResponseEntity}
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn("Handling 'no handler found exception' for: {}", request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .cause(ex.getMessage())
                        .build());
    }

    /**
     * In case of target entity was not found in database
     *
     * @param ex throwed exception
     * @return business {@link ResponseEntity}
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final Throwable ex) {
        log.debug("Handling 'not found exception': {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .cause(ex.getMessage())
                        .build());
    }

    /**
     * In case of any validation exceptions occurred on any entity
     *
     * @param ex throwed exception
     * @return business {@link ResponseEntity}
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final Throwable ex) {
        log.debug("Handling 'validation exception': {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .cause(ex.getMessage())
                        .build());
    }

}
