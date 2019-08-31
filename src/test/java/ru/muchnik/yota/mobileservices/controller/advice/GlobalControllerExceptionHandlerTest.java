package ru.muchnik.yota.mobileservices.controller.advice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.muchnik.yota.mobileservices.model.ErrorResponse;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    private GlobalControllerExceptionHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new GlobalControllerExceptionHandler();
    }

    @Test
    public void handleRuntimeException() {

        ResponseEntity<ErrorResponse> result = handler.handleRuntimeException(new RuntimeException("text"));

        ErrorResponse exp = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .cause("text")
                .build();
        Assert.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp), result);
    }

    @Test
    public void handleNotFoundException() {
        ResponseEntity<ErrorResponse> result = handler.handleNotFoundException(new NotFoundException("text"));

        ErrorResponse exp = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .cause("text")
                .build();
        Assert.assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp), result);
    }

    @Test
    public void handleIllegalState() {
        ResponseEntity<ErrorResponse> result = handler.handleValidationException(new IllegalStateException("text"));

        ErrorResponse exp = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .cause("text")
                .build();
        Assert.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp), result);
    }

    @Test
    public void handleValidationException() {
        ResponseEntity<ErrorResponse> result = handler.handleValidationException(new ValidationException("text"));

        ErrorResponse exp = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .cause("text")
                .build();
        Assert.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp), result);
    }
}