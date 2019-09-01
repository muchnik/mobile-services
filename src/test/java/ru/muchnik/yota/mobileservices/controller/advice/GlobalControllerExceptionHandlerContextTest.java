package ru.muchnik.yota.mobileservices.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.muchnik.yota.mobileservices.controller.MinutesPackageController;
import ru.muchnik.yota.mobileservices.model.ErrorResponse;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest({GlobalControllerExceptionHandler.class, MinutesPackageController.class})
@AutoConfigureMockMvc
public class GlobalControllerExceptionHandlerContextTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandLineRunner runner; // mocking because of this bean is executed when application starts
    @MockBean
    private MinutesPackageService packageService;
    @MockBean
    private MinutesDetailsService detailsService;

    @Test
    public void handleRuntimeException() throws Exception {
        when(packageService.getPackage(eq("1"))).thenThrow(new RuntimeException("error"));

        ErrorResponse response = ErrorResponse.builder()
                .cause("error")
                .code(500)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        mockMvc.perform(get("/api/v1/packages-of-minutes/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    public void handleNoHandlerFoundException() throws Exception {
        ErrorResponse response = ErrorResponse.builder()
                .cause("No handler found for GET /wrongUrl")
                .code(404)
                .status(HttpStatus.NOT_FOUND)
                .build();

        mockMvc.perform(get("/wrongUrl").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    public void handleNotFoundException() throws Exception {
        when(packageService.getPackage(eq("1"))).thenThrow(new NotFoundException("error"));

        ErrorResponse response = ErrorResponse.builder()
                .cause("error")
                .code(404)
                .status(HttpStatus.NOT_FOUND)
                .build();

        mockMvc.perform(get("/api/v1/packages-of-minutes/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    public void handleValidationException() throws Exception {
        when(packageService.getPackage(eq("1"))).thenThrow(new ValidationException("error"));

        ErrorResponse response = ErrorResponse.builder()
                .cause("error")
                .code(500)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        mockMvc.perform(get("/api/v1/packages-of-minutes/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}