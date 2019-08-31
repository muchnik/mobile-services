package ru.muchnik.yota.mobileservices.controller;

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
import org.springframework.test.web.servlet.MvcResult;
import ru.muchnik.yota.mobileservices.model.ErrorResponse;
import ru.muchnik.yota.mobileservices.model.dto.UnitDTO;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.service.MinutesPackageDetailsService;
import ru.muchnik.yota.mobileservices.service.SimCardService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SimCardController.class)
@AutoConfigureMockMvc
public class SimCardControllerContextTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandLineRunner runner; // mocking because of this bean is executed when application starts

    @MockBean
    private SimCardService simCardService;

    @MockBean
    private MinutesPackageDetailsService detailsService;

    private SimCard simCard = new SimCard();
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getSimCard() throws Exception {
        when(simCardService.getSimCard(eq("1"))).thenReturn(simCard);

        mockMvc.perform(get("/api/v1/sim-card/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(simCard)));
    }

    @Test
    public void getSimCardNotFound() throws Exception {
        when(simCardService.getSimCard(eq("1"))).thenThrow(new NotFoundException("text"));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(404)
                .cause("text")
                .build();

        mockMvc.perform(get("/api/v1/sim-card/1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(errorResponse)));
    }

    @Test
    public void getSimCardStatus() throws Exception {
        UnitDTO<Boolean> dto = new UnitDTO<>(true);
        when(simCardService.getSimCardStatus(eq("1"))).thenReturn(true);

        mockMvc.perform(get("/api/v1/sim-card/1/status").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    public void updateSimCardStatus() throws Exception {
        UnitDTO<Boolean> dto = new UnitDTO<>(true);
        when(simCardService.updateSimCardStatus(eq("1"), eq(true))).thenReturn(true);

        mockMvc.perform(put("/api/v1/sim-card/1/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"value\": true }"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(dto)));

        verify(simCardService).updateSimCardStatus(eq("1"), eq(true));
    }

    @Test
    public void getSimCardActiveMinutesTotal() throws Exception {
        UnitDTO<Integer> dto = new UnitDTO<>(22);
        PackageOfMinutes pof = PackageOfMinutes.builder()
                .name("name")
                .type(PackageOfMinutes.PackageOfMinutesType.FAVORITE_NUMBER)
                .build();
        MinutesPackageDetails details = new MinutesPackageDetails(pof, 10, 15);
        MinutesPackageDetails details2 = new MinutesPackageDetails(pof, 12, 17);
        List<MinutesPackageDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);

        when(detailsService.getAllActivePackages(eq("1"))).thenReturn(list);

        mockMvc.perform(get("/api/v1/sim-card/1/minutes/total")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    public void getSimCardActivePackagesOfMinutes() throws Exception {
        PackageOfMinutes pof = PackageOfMinutes.builder()
                .name("name")
                .type(PackageOfMinutes.PackageOfMinutesType.FAVORITE_NUMBER)
                .build();
        MinutesPackageDetails details = new MinutesPackageDetails(pof, 10, 15);
        MinutesPackageDetails details2 = new MinutesPackageDetails(pof, 12, 17);
        details.setSimCard(simCard);
        details2.setSimCard(simCard);
        List<MinutesPackageDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);

        when(detailsService.getAllActivePackages(eq("1"))).thenReturn(list);

        MvcResult result = mockMvc.perform(get("/api/v1/sim-card/1/minutes/packages")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        String content = result.getResponse().getContentAsString();
//        List<MinutesPackageDetails> resultContent = mapper.readValue(content, new TypeReference<List<MinutesPackageDetails>>() {});
//        Assert.assertEquals(details, resultContent.get(0));
//        Assert.assertEquals(details2, resultContent.get(1));
        // todo
    }

    @Test
    public void addPackageOfMinutesToSimCard() throws Exception {
        MinutesPackageDetails details = new MinutesPackageDetails();

        when(simCardService.addPackageOfMinutesToSimCard(eq("1"), anyLong(), anyInt(), anyInt()))
                .thenReturn(details);
        String id = details.getId();

        mockMvc.perform(post("/api/v1/sim-card/1/minutes")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"basePackageId\": 1, \"minutes\": 15, \"daysToLive\": 10}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("Location", "/api/v1/package-of-minutes/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(details)));

        verify(simCardService).addPackageOfMinutesToSimCard(eq("1"), eq(1L), eq(15), eq(10));
    }
}