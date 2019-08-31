package ru.muchnik.yota.mobileservices.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.muchnik.yota.mobileservices.model.dto.ValueDTO;
import ru.muchnik.yota.mobileservices.model.dto.UpdatePackageRequestDTO;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.service.BaseDetailsService;
import ru.muchnik.yota.mobileservices.service.SimCardService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimCardControllerTest {
    @Mock
    private SimCardService simCardService;
    @Mock
    private MinutesDetailsService detailsService;

    @InjectMocks
    private SimCardController controller;

    @Mock
    private SimCard simCard;
    @Mock
    private MinutesDetails details;
    @Mock
    private MinutesDetails details2;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getSimCard() {
        ResponseEntity<SimCard> exp = ResponseEntity.ok(simCard);
        when(simCardService.getSimCard(eq("89992223344"))).thenReturn(simCard);

        ResponseEntity<SimCard> result = controller.getSimCard("89992223344");
        Assert.assertEquals(exp, result);
    }

    @Test
    public void getSimCardStatus() {
        ResponseEntity<ValueDTO<Boolean>> exp = ResponseEntity.ok(new ValueDTO<>(true));
        when(simCardService.getSimCardStatus(eq("89992223344"))).thenReturn(true);

        ResponseEntity<ValueDTO<Boolean>> result = controller.getSimCardStatus("89992223344");
        Assert.assertEquals(exp, result);
    }

    @Test
    public void updateSimCardStatus() {
        controller.updateSimCardStatus("89992223344", new ValueDTO<>(true));

        verify(simCardService).updateSimCardStatus(eq("89992223344"), eq(true));
    }

    @Test
    public void getSimCardActiveMinutesValue() {
        List<MinutesDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);
        when(details.getMinutesLeft()).thenReturn(2);
        when(details2.getMinutesLeft()).thenReturn(3);
        when(detailsService.getAllActivePackages(eq("89992223344"))).thenReturn(list);

        ResponseEntity<ValueDTO<Integer>> result = controller.getSimCardActiveMinutesTotal("89992223344");

        ResponseEntity<ValueDTO<Integer>> exp = ResponseEntity.ok(new ValueDTO<>(5));
        Assert.assertEquals(exp, result);
    }

    @Test
    public void getSimCardActiveMinutes() {
        List<MinutesDetails> list = new ArrayList<>();
        list.add(details);
        list.add(details2);
        when(detailsService.getAllActivePackages(eq("89992223344"))).thenReturn(list);

        ResponseEntity<List<MinutesDetails>> result = controller.getSimCardActivePackagesOfMinutes("89992223344");

        ResponseEntity<List<MinutesDetails>> exp = ResponseEntity.ok(list);
        Assert.assertEquals(exp, result);
    }

    @Test
    public void addSimCardMinutesPackage() {
        when(simCardService.addPackageOfMinutesToSimCard(eq("89992223344"), eq("1"), eq(2), eq(3)))
                .thenReturn(details);
        when(details.getId()).thenReturn("1");

        ResponseEntity<MinutesDetails> result = controller.addPackageOfMinutesToSimCard("89992223344", new UpdatePackageRequestDTO("1", 2, 3));

        verify(simCardService).addPackageOfMinutesToSimCard(eq("89992223344"), eq("1"), eq(2), eq(3));
        Assert.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }
}