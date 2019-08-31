package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;
import ru.muchnik.yota.mobileservices.repository.MinutesPackageDetailsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PackageOfMinutesDetailsServiceTest {
    @Mock
    private MinutesPackageDetailsRepository repository;

    @InjectMocks
    private MinutesPackageDetailsService service;

    @Mock
    private MinutesPackageDetails details;
    private Optional<MinutesPackageDetails> optionalMinutesPackageDetails;

    @Before
    public void setUp() throws Exception {
        optionalMinutesPackageDetails = Optional.of(details);
    }

    @Test
    public void getDetails() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackageDetails);

        MinutesPackageDetails result = service.getDetails(1L);

        Assert.assertEquals(details, result);
    }

    @Test(expected = NotFoundException.class)
    public void getDetailsNotFound() {
        when(repository.findById(eq(1L))).thenReturn(Optional.empty());

        MinutesPackageDetails result = service.getDetails(1L);
    }

    @Test
    public void updateMinutesAdding() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getMinutesLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateMinutes(1L, 5);

        verify(details).setMinutesLeft(eq(15));
    }

    @Test
    public void updateMinutesDecrement() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getMinutesLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateMinutes(1L, -5);

        verify(details).setMinutesLeft(eq(5));
    }

    @Test(expected = ValidationException.class)
    public void updateMinutesAddingButExpired() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(2));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().minusDays(1));

        service.updateMinutes(1L, 5);

        verify(details, times(0)).setMinutesLeft(anyInt());
    }

    @Test(expected = ValidationException.class)
    public void updateMinutesDecrementException() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getMinutesLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateMinutes(1L, -15);

        verify(details, times(0)).setMinutesLeft(anyInt());
    }

    @Test
    public void getAllActivePackages() {
        when(repository.findAllActive(eq("89992223344"), any(LocalDateTime.class))).thenReturn(Collections.singletonList(details));

        List<MinutesPackageDetails> result = service.getAllActivePackages("89992223344");

        verify(repository).findAllActive(eq("89992223344"), any(LocalDateTime.class));
        Assert.assertEquals(Collections.singletonList(details), result);
    }
}