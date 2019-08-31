package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.MinutesPackageDetailsRepository;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimCardServiceTest {
    private static final String PHONE_NUMBER = "98883339988";

    @Mock
    private SimCardRepository simCardRepository;
    @Mock
    private PackageOfMinutesService packageOfMinutesService;
    @Mock
    private MinutesPackageDetailsRepository detailsRepository;

    @InjectMocks
    private SimCardService service;

    @Mock
    private SimCard simCard;
    private Optional<SimCard> optionalSimCard;

    @Mock
    private PackageOfMinutes basePackage;

    @Before
    public void setUp() throws Exception {
        optionalSimCard = Optional.of(simCard);
    }

    @Test
    public void getSimCard() {
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(optionalSimCard);

        SimCard result = service.getSimCard(PHONE_NUMBER);
        Assert.assertEquals(simCard, result);
    }

    @Test(expected = NotFoundException.class)
    public void getSimCardNotFound() {
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(Optional.empty());

        service.getSimCard(PHONE_NUMBER);
    }

    @Test
    public void getSimCardStatus() {
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(optionalSimCard);
        when(simCard.isActive()).thenReturn(true);

        boolean result = service.getSimCardStatus(PHONE_NUMBER);
        Assert.assertTrue(result);
    }

    @Test
    public void updateSimCardStatus() {
        when(simCardRepository.updateStatus(eq(PHONE_NUMBER), eq(false))).thenReturn(1);
        boolean updated = service.updateSimCardStatus(PHONE_NUMBER, false);
        verify(simCardRepository).updateStatus(eq(PHONE_NUMBER), eq(false));
        Assert.assertTrue(updated);
    }

    @Test
    public void updateSimCardStatusNotUpdated() {
        when(simCardRepository.updateStatus(eq(PHONE_NUMBER), eq(false))).thenReturn(0);
        boolean updated = service.updateSimCardStatus(PHONE_NUMBER, false);
        verify(simCardRepository).updateStatus(eq(PHONE_NUMBER), eq(false));
        Assert.assertFalse(updated);
    }

    @Test
    public void addMinutesPackageToSimCard() {
        LocalDateTime plusDays = LocalDateTime.now().plusDays(3);
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(optionalSimCard);
        when(packageOfMinutesService.getPackage(eq(1L))).thenReturn(basePackage);
        ArrayList<MinutesPackageDetails> list = spy(new ArrayList<>());
        when(simCard.getMinutesPackageDetails()).thenReturn(list);

        service.addPackageOfMinutesToSimCard(PHONE_NUMBER, 1L, 2, 3);
        LocalDateTime now = LocalDateTime.now();

        verify(list).add(any(MinutesPackageDetails.class));
        MinutesPackageDetails resultDetails = list.get(0);
        Assert.assertEquals(2, resultDetails.getMinutesLeft());
        Assert.assertEquals(simCard, resultDetails.getSimCard());
        Assert.assertEquals(basePackage, resultDetails.getBasePackage());
        Assert.assertTrue(resultDetails.getExpirationDate().isAfter(plusDays));
        Assert.assertTrue(resultDetails.getActivationDate().compareTo(now) <= 0);
    }
}