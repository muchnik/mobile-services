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
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinutesPackageCatalogControllerTest {
    @Mock
    private MinutesPackageService packageService;

    @Mock
    private MinutesDetailsService detailsService;

    @InjectMocks
    private MinutesPackageController controller;

    @Mock
    private MinutesPackageCatalog minutesPackageCatalog;
    @Mock
    private MinutesPackageCatalog minutesPackageCatalog2;

    @Captor
    private ArgumentCaptor<MinutesPackageCatalog> captor;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getPackage() {
        when(packageService.getPackage(eq("1"))).thenReturn(minutesPackageCatalog);

        ResponseEntity<MinutesPackageCatalog> result = controller.getPackage("1");

        verify(packageService).getPackage(eq("1"));
        Assert.assertEquals(ResponseEntity.ok(minutesPackageCatalog), result);
    }

    @Test
    public void getPackages() {
        when(packageService.getPackages()).thenReturn(Arrays.asList(minutesPackageCatalog, minutesPackageCatalog2));

        ResponseEntity<List<MinutesPackageCatalog>> result = controller.getPackages();

        verify(packageService).getPackages();
        Assert.assertEquals(ResponseEntity.ok(Arrays.asList(minutesPackageCatalog, minutesPackageCatalog2)), result);
    }


    @Test
    public void savePackage() {
        when(packageService.savePackage(captor.capture())).thenReturn(minutesPackageCatalog);
        when(minutesPackageCatalog.getId()).thenReturn("2");

        ResponseEntity<MinutesPackageCatalog> result = controller.savePackage(MinutesPackageCatalog.builder()
                .name("name")
                .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                .build());

        MinutesPackageCatalog captorValue = captor.getValue();
        Assert.assertEquals("name", captorValue.getName());
        Assert.assertEquals(MinutesPackageCatalog.Type.FAVORITE_NUMBER, captorValue.getType());
        Assert.assertEquals(ResponseEntity.created(URI.create("/api/v1/packages-of-minutes/2")).body(minutesPackageCatalog), result);
    }

    @Test
    public void updateMinutes() {
        ResponseEntity<Void> voidResponseEntity = controller.updatePackageAmount("1", new ValueDTO<>(2));

        verify(detailsService).updateAmount(eq("1"), eq(2));
        Assert.assertEquals(200, voidResponseEntity.getStatusCodeValue());
    }

    @Test
    public void updateMinutesBy0() {
        ResponseEntity<Void> voidResponseEntity = controller.updatePackageAmount("1", new ValueDTO<>(0));

        verifyZeroInteractions(detailsService);
        Assert.assertEquals(200, voidResponseEntity.getStatusCodeValue());
    }
}