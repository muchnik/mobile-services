package ru.muchnik.yota.mobileservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MinutesPackageController.class)
@AutoConfigureMockMvc
public class MinutesPackageCatalogControllerContextTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandLineRunner runner; // mocking because of this bean is executed when application starts
    @MockBean
    private MinutesPackageService packageService;
    @MockBean
    private MinutesDetailsService detailsService;
    private MinutesPackageCatalog minutesPackageCatalog = new MinutesPackageCatalog();
    @Captor
    private ArgumentCaptor<MinutesPackageCatalog> captor;

    @Test
    public void getPackage() throws Exception {
        when(packageService.getPackage(eq("1"))).thenReturn(minutesPackageCatalog);

        mockMvc.perform(get("/api/v1/packages-of-minutes/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(minutesPackageCatalog)));

    }

    @Test
    public void savePackage() throws Exception {
        MinutesPackageCatalog mp = MinutesPackageCatalog.builder()
                .name("name")
                .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                .build();
        when(packageService.savePackage(any())).thenReturn(mp);

        mockMvc.perform(post("/api/v1/packages-of-minutes/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"name\": \"name\", \"type\": \"FAVORITE_NUMBER\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("Location", "/api/v1/packages-of-minutes/" + mp.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(mp)));

        verify(packageService).savePackage(captor.capture());
        MinutesPackageCatalog captorValue = captor.getValue();
        Assert.assertEquals("name", captorValue.getName());
        Assert.assertEquals(MinutesPackageCatalog.Type.FAVORITE_NUMBER, captorValue.getType());
    }

    @Test
    public void updateMinutes() throws Exception {
        mockMvc.perform(put("/api/v1/packages-of-minutes/details/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"value\": 15}"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(detailsService).updateAmount(eq("1"), eq(15));
    }
}