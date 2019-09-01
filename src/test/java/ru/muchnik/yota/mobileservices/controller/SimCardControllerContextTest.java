package ru.muchnik.yota.mobileservices.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
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
import ru.muchnik.yota.mobileservices.model.dto.ValueDTO;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.service.SimCardService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficDetailsService;

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
    private MinutesDetailsService detailsService;

    @MockBean
    private TrafficDetailsService trafficDetailsService;

    private SimCard simCard = new SimCard();
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        mapper.registerModule(new JavaTimeModule());
    }

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
        ValueDTO<Boolean> dto = new ValueDTO<>(true);
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
        when(simCardService.updateSimCardStatus(eq("1"), eq(true))).thenReturn(simCard);

        mockMvc.perform(put("/api/v1/sim-card/1/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"value\": true }"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(simCard)));

        verify(simCardService).updateSimCardStatus(eq("1"), eq(true));
    }

    @Test
    public void getSimCardActiveMinutesTotal() throws Exception {
        ValueDTO<Integer> dto = new ValueDTO<>(22);
        MinutesPackageCatalog pof = MinutesPackageCatalog.builder()
                .name("name")
                .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                .build();
        MinutesDetails details = new MinutesDetails(pof, 10, 15);
        MinutesDetails details2 = new MinutesDetails(pof, 12, 17);
        List<MinutesDetails> list = new ArrayList<>();
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
    public void getSimCardActiveTrafficTotal() throws Exception {
        ValueDTO<Integer> dto = new ValueDTO<>(22);
        TrafficPackageCatalog tpc = TrafficPackageCatalog.builder()
                .name("name")
                .type(TrafficPackageCatalog.Type.YOUTUBE)
                .build();
        TrafficDetails details = new TrafficDetails(tpc, 10, 15);
        TrafficDetails details2 = new TrafficDetails(tpc, 12, 17);
        List<TrafficDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);

        when(trafficDetailsService.getAllActivePackages(eq("1"))).thenReturn(list);

        mockMvc.perform(get("/api/v1/sim-card/1/traffic/total")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    public void getSimCardActiveMinutesPackages() throws Exception {
        MinutesPackageCatalog pof = MinutesPackageCatalog.builder()
                .name("name")
                .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                .build();
        MinutesDetails details = new MinutesDetails(pof, 10, 15);
        MinutesDetails details2 = new MinutesDetails(pof, 12, 17);
        details.setSimCard(simCard);
        details2.setSimCard(simCard);
        List<MinutesDetails> list = new ArrayList<>();
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
        List<MinutesDetails> resultContent = mapper.readValue(content, new TypeReference<List<MinutesDetails>>() {
        });
        Assert.assertEquals(list, resultContent);
    }

    @Test
    public void getSimCardActiveTrafficPackages() throws Exception {
        TrafficPackageCatalog pof = TrafficPackageCatalog.builder()
                .name("name")
                .type(TrafficPackageCatalog.Type.YOUTUBE)
                .build();
        TrafficDetails details = new TrafficDetails(pof, 10, 15);
        TrafficDetails details2 = new TrafficDetails(pof, 12, 17);
        details.setSimCard(simCard);
        details2.setSimCard(simCard);
        List<TrafficDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);

        when(trafficDetailsService.getAllActivePackages(eq("1"))).thenReturn(list);

        MvcResult result = mockMvc.perform(get("/api/v1/sim-card/1/traffic/packages")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<TrafficDetails> resultContent = mapper.readValue(content, new TypeReference<List<TrafficDetails>>() {
        });
        Assert.assertEquals(list, resultContent);
    }

    @Test
    public void addPackageOfMinutesToSimCard() throws Exception {
        MinutesDetails details = new MinutesDetails();

        when(simCardService.addPackageOfMinutesToSimCard(eq("1"), anyString(), anyInt(), anyInt()))
                .thenReturn(details);
        String id = details.getId();

        mockMvc.perform(post("/api/v1/sim-card/1/minutes")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"basePackageId\": 1, \"addition\": 15, \"daysToLive\": 10}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("Location", "/api/v1/packages-of-minutes/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(details)));

        verify(simCardService).addPackageOfMinutesToSimCard(eq("1"), eq("1"), eq(15), eq(10));
    }

    @Test
    public void addTrafficPackageToSimCard() throws Exception {
        TrafficDetails details = new TrafficDetails();

        when(simCardService.addTrafficPackageToSimCard(eq("1"), anyString(), anyInt(), anyInt()))
                .thenReturn(details);
        String id = details.getId();

        mockMvc.perform(post("/api/v1/sim-card/1/traffic")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"basePackageId\": 1, \"addition\": 15, \"daysToLive\": 10}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("Location", "/api/v1/traffic-packages/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(details)));

        verify(simCardService).addTrafficPackageToSimCard(eq("1"), eq("1"), eq(15), eq(10));
    }
}