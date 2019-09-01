package ru.muchnik.yota.mobileservices.repository;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesDetailsRepository;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesPackageCatalogRepository;
import ru.muchnik.yota.mobileservices.repository.traffic.TrafficPackageCatalogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MobileServicesDataJpaTest {
    @Autowired
    private MinutesDetailsRepository detailsRepository;
    @Autowired
    private MinutesPackageCatalogRepository packageRepository;
    @Autowired
    private SimCardRepository simCardRepository;
    @Autowired
    private TrafficPackageCatalogRepository trafficPackageCatalogRepository;

    @MockBean
    private CommandLineRunner runner; // we dont need to fill database by test data from working context

    private List<MinutesDetails> expectedActiveDetails;
    private SimCard simCard;

    @Before
    public void setUp() throws Exception {
        MinutesPackageCatalog freeRoaming = MinutesPackageCatalog.builder()
                .name("FreeRoaming")
                .type(MinutesPackageCatalog.Type.FREE_ROAMING)
                .build();

        MinutesPackageCatalog favoriteNumber = MinutesPackageCatalog.builder()
                .name("FavoriteNumber")
                .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                .build();

        MinutesDetails activeFirstDetails = new MinutesDetails(freeRoaming, 300, 30);
        MinutesDetails expiredDetails = new MinutesDetails(freeRoaming, 200, -1);
        MinutesDetails activeSecondDetails = new MinutesDetails(freeRoaming, 100, 1);

        TrafficPackageCatalog trafficYoutube = TrafficPackageCatalog.builder()
                .name("Youtube")
                .type(TrafficPackageCatalog.Type.YOUTUBE)
                .build();

        TrafficDetails trafficDetails = new TrafficDetails(trafficYoutube, 4, 2);
        TrafficDetails expiredTraffic = new TrafficDetails(trafficYoutube, 5, -1);
        TrafficDetails trafficDetails2 = new TrafficDetails(trafficYoutube, 6, 3);

        simCard = SimCard.builder()
                .number("89998887766")
                .isActive(true)
                .minutesDetails(Arrays.asList(activeFirstDetails, expiredDetails, activeSecondDetails))
                .trafficDetails(Arrays.asList(trafficDetails, expiredTraffic, trafficDetails2))
                .build();

        activeFirstDetails.setSimCard(simCard);
        expiredDetails.setSimCard(simCard);
        activeSecondDetails.setSimCard(simCard);

        trafficDetails.setSimCard(simCard);
        expiredTraffic.setSimCard(simCard);
        trafficDetails2.setSimCard(simCard);

        expectedActiveDetails = new ArrayList<>();
        expectedActiveDetails.add(activeFirstDetails);
        expectedActiveDetails.add(activeSecondDetails);

        trafficPackageCatalogRepository.saveAll(Arrays.asList(trafficYoutube));
        packageRepository.saveAll(Arrays.asList(favoriteNumber, freeRoaming));
        simCardRepository.saveAndFlush(simCard);
    }

    /******************************************
     *  MinutesPackageDetailsRepository tests  *
     ******************************************/

    @Test
    public void findAllActiveOneDetailsIsExpired() {
        List<MinutesDetails> result = detailsRepository.findAllActive("89998887766", LocalDateTime.now());
        Assert.assertEquals(expectedActiveDetails, result);
    }

    @Test
    public void findAllActiveNoneExists() {
        detailsRepository.deleteAllInBatch();
        List<MinutesDetails> result = detailsRepository.findAllActive("89998887766", LocalDateTime.now());
        Assert.assertTrue(result.isEmpty());
    }

    /*****************************
     *  SimCardRepository tests  *
     ****************************/
    @Test
    public void getByNumber() {
        SimCard foundedSimCard = simCardRepository.getByNumber("89998887766").get();

        Assert.assertEquals(simCard.getId(), foundedSimCard.getId());
        Assert.assertEquals(simCard.getNumber(), foundedSimCard.getNumber());
        Assert.assertEquals(simCard.getMinutesDetails(), foundedSimCard.getMinutesDetails());
    }

    @Test
    public void getByNumberNotFound() {
        Optional<SimCard> foundedSimCard = simCardRepository.getByNumber("777");

        Assert.assertFalse(foundedSimCard.isPresent());
    }
}