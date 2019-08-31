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
import ru.muchnik.yota.mobileservices.model.dto.UnitDTO;
import ru.muchnik.yota.mobileservices.model.dto.request.AddPackageOfMinutesRequestDTO;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.service.MinutesPackageDetailsService;
import ru.muchnik.yota.mobileservices.service.PackageOfMinutesService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PackageOfMinutesControllerTest {
    @Mock
    private PackageOfMinutesService packageService;

    @Mock
    private MinutesPackageDetailsService detailsService;

    @InjectMocks
    private PackageOfMinutesController controller;

    @Mock
    private PackageOfMinutes packageOfMinutes;

    @Captor
    private ArgumentCaptor<PackageOfMinutes> captor;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getPackage() {
        controller.getPackage(1L);

        verify(packageService).getPackage(eq(1L));
    }

    @Test
    public void savePackage() {
        when(packageService.savePackage(captor.capture())).thenReturn(packageOfMinutes);
        when(packageOfMinutes.getId()).thenReturn("2");

        controller.savePackage(new AddPackageOfMinutesRequestDTO("name", PackageOfMinutes.PackageOfMinutesType.FAVORITE_NUMBER));

        PackageOfMinutes captorValue = captor.getValue();
        Assert.assertEquals("name", captorValue.getName());
        Assert.assertEquals(PackageOfMinutes.PackageOfMinutesType.FAVORITE_NUMBER, captorValue.getType());
    }

    @Test
    public void updateMinutes() {
        controller.updateMinutes(1L, new UnitDTO<>(2));

        verify(detailsService).updateMinutes(eq(1L), eq(2));
    }
}