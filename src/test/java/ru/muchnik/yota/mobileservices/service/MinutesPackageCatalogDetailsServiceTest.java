package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesDetailsRepository;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinutesPackageCatalogDetailsServiceTest {
    @Mock
    private MinutesDetailsRepository repository;

    @InjectMocks
    private MinutesDetailsService service;

    @Mock
    private MinutesDetails details;
    private Optional<MinutesDetails> optionalMinutesPackageDetails;

    @Before
    public void setUp() throws Exception {
        optionalMinutesPackageDetails = Optional.of(details);
    }

    @Test
    public void getDetails() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackageDetails);

        MinutesDetails result = service.getDetails("1");

        Assert.assertEquals(details, result);
    }

    @Test(expected = NotFoundException.class)
    public void getDetailsNotFound() {
        when(repository.findById(eq("1"))).thenReturn(Optional.empty());

        MinutesDetails result = service.getDetails("1");
    }

    @Test
    public void updateMinutesAdding() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getAmountLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateAmount("1", 5);

        verify(details).setAmountLeft(eq(15));
    }

    @Test
    public void updateMinutesDecrement() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getAmountLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateAmount("1", -5);

        verify(details).setAmountLeft(eq(5));
    }

    @Test(expected = ValidationException.class)
    public void updateMinutesAddingButExpired() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(2));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().minusDays(1));

        service.updateAmount("1", 5);

        verify(details, times(0)).setMinutesLeft(anyInt());
    }

    @Test(expected = ValidationException.class)
    public void updateMinutesDecrementException() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackageDetails);

        // validation
        when(details.getAmountLeft()).thenReturn(10);
        when(details.getActivationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(details.getExpirationDate()).thenReturn(LocalDateTime.now().plusDays(1));

        service.updateAmount("1", -15);

        verify(details, times(0)).setMinutesLeft(anyInt());
    }

    @Test
    public void getAllActivePackages() {
        when(repository.findAllActive(eq("89992223344"), any(LocalDateTime.class))).thenReturn(Collections.singletonList(details));

        List<MinutesDetails> result = service.getAllActivePackages("89992223344");

        verify(repository).findAllActive(eq("89992223344"), any(LocalDateTime.class));
        Assert.assertEquals(Collections.singletonList(details), result);
    }
}