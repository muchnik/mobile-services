package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesDetailsRepository;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

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
    private MinutesPackageService minutesPackageService;
    @Mock
    private MinutesDetailsRepository detailsRepository;

    @InjectMocks
    private SimCardService service;

    @Mock
    private SimCard simCard;
    private Optional<SimCard> optionalSimCard;

    @Mock
    private MinutesPackageCatalog basePackage;

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
        SimCard simCard = SimCard.builder()
                .number(PHONE_NUMBER)
                .isActive(true)
                .build();
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(Optional.of(simCard));

        service.updateSimCardStatus(PHONE_NUMBER, false);

        Assert.assertFalse(simCard.isActive());
    }

    @Test (expected = NotFoundException.class)
    public void updateSimCardStatusNotUpdated() {
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(Optional.empty());

        service.updateSimCardStatus(PHONE_NUMBER, false);
    }

    @Test
    public void addMinutesPackageToSimCard() {
        LocalDateTime plusDays = LocalDateTime.now().plusDays(3);
        when(simCardRepository.getByNumber(eq(PHONE_NUMBER))).thenReturn(optionalSimCard);
        when(minutesPackageService.getPackage(eq("1"))).thenReturn(basePackage);
        ArrayList<MinutesDetails> list = spy(new ArrayList<>());
        when(simCard.getMinutesDetails()).thenReturn(list);

        service.addPackageOfMinutesToSimCard(PHONE_NUMBER, "1", 2, 3);
        LocalDateTime now = LocalDateTime.now();

        verify(list).add(any(MinutesDetails.class));
        MinutesDetails resultDetails = list.get(0);
        Assert.assertEquals(2, resultDetails.getMinutesLeft());
        Assert.assertEquals(simCard, resultDetails.getSimCard());
        Assert.assertEquals(basePackage, resultDetails.getBasePackage());
        Assert.assertTrue(resultDetails.getExpirationDate().isAfter(plusDays));
        Assert.assertTrue(resultDetails.getActivationDate().compareTo(now) <= 0);
    }
}