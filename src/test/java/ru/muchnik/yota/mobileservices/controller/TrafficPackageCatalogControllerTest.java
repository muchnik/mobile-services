package ru.muchnik.yota.mobileservices.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import ru.muchnik.yota.mobileservices.model.dto.ValueDTO;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficDetailsService;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficPackageService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrafficPackageCatalogControllerTest {
    @Mock
    private TrafficPackageService packageService;

    @Mock
    private TrafficDetailsService detailsService;

    @InjectMocks
    private TrafficPackageController controller;

    @Mock
    private TrafficPackageCatalog trafficPackageCatalog;
    @Mock
    private TrafficPackageCatalog minutespackagecatalog2;

    @Captor
    private ArgumentCaptor<TrafficPackageCatalog> captor;

    @Test
    public void getPackage() {
        when(packageService.getPackage(eq("1"))).thenReturn(trafficPackageCatalog);

        ResponseEntity<TrafficPackageCatalog> result = controller.getPackage("1");

        verify(packageService).getPackage(eq("1"));
        Assert.assertEquals(ResponseEntity.ok(trafficPackageCatalog), result);
    }

    @Test
    public void getPackages() {
        when(packageService.getPackages()).thenReturn(Arrays.asList(trafficPackageCatalog, minutespackagecatalog2));

        ResponseEntity<List<TrafficPackageCatalog>> result = controller.getPackages();

        verify(packageService).getPackages();
        Assert.assertEquals(ResponseEntity.ok(Arrays.asList(trafficPackageCatalog, minutespackagecatalog2)), result);
    }


    @Test
    public void savePackage() {
        when(packageService.savePackage(captor.capture())).thenReturn(trafficPackageCatalog);
        when(trafficPackageCatalog.getId()).thenReturn("2");

        ResponseEntity<TrafficPackageCatalog> result = controller.savePackage(TrafficPackageCatalog.builder()
                .name("name")
                .type(TrafficPackageCatalog.TrafficPackageType.YOUTUBE)
                .build());

        TrafficPackageCatalog captorValue = captor.getValue();
        Assert.assertEquals("name", captorValue.getName());
        Assert.assertEquals(TrafficPackageCatalog.TrafficPackageType.YOUTUBE, captorValue.getType());
        Assert.assertEquals(ResponseEntity.created(URI.create("/api/v1/traffic-packages/2")).body(trafficPackageCatalog), result);
    }

    @Test
    public void updateMinutes() {
        ResponseEntity<Void> voidResponseEntity = controller.updateAmount("1", new ValueDTO<>(2));

        verify(detailsService).updateAmount(eq("1"), eq(2));
        Assert.assertEquals(200, voidResponseEntity.getStatusCodeValue());
    }

    @Test
    public void updateMinutesBy0() {
        ResponseEntity<Void> voidResponseEntity = controller.updateAmount("1", new ValueDTO<>(0));

        verifyZeroInteractions(detailsService);
        Assert.assertEquals(200, voidResponseEntity.getStatusCodeValue());
    }
}