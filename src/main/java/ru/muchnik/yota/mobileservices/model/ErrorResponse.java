package ru.muchnik.yota.mobileservices.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * In case of exceptions that being throwed in {@link org.springframework.web.bind.annotation.RestController}
 * that was catched via {@link org.springframework.web.bind.annotation.ControllerAdvice} for further response to client
 *
 * @see ru.muchnik.yota.mobileservices.controller.advice.GlobalControllerExceptionHandler
 */
@Data
@Builder
public class ErrorResponse {
    private final String cause;
    private final int code;
    private final HttpStatus status;
}
