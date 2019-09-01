package ru.muchnik.yota.mobileservices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesPackageCatalogRepository;
import ru.muchnik.yota.mobileservices.repository.traffic.TrafficPackageCatalogRepository;

import java.util.Arrays;

/**
 * Application for "YOTA" test task, see readme.md for more information
 *
 * @author Muchnik Andrey <b>muchnik.ak@gmail.com</b>
 * @since 01.09.2019
 */
@SpringBootApplication
public class MobileServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MobileServicesApplication.class, args);
    }

    /**
     * Filling database with test data, when application starts
     *
     * @apiNote <b>Just for testing purposes as a part of test task</b>
     */
    @Bean
    public CommandLineRunner runner(SimCardRepository simCardRepository, MinutesPackageCatalogRepository minutesRepository, TrafficPackageCatalogRepository trafficRepository) {
        return args -> {
            MinutesPackageCatalog freeRoaming = MinutesPackageCatalog.builder()
                    .name("FreeRoaming")
                    .type(MinutesPackageCatalog.Type.FREE_ROAMING)
                    .build();

            MinutesPackageCatalog favoriteNumber = MinutesPackageCatalog.builder()
                    .name("FavoriteNumber")
                    .type(MinutesPackageCatalog.Type.FAVORITE_NUMBER)
                    .build();

            MinutesDetails packageDetails = new MinutesDetails(freeRoaming, 300, 30);
            MinutesDetails packageDetails2 = new MinutesDetails(freeRoaming, 200, -1);
            MinutesDetails packageDetails3 = new MinutesDetails(freeRoaming, 100, 1);

            TrafficPackageCatalog trafficYoutube = TrafficPackageCatalog.builder()
                    .name("Youtube")
                    .type(TrafficPackageCatalog.Type.YOUTUBE)
                    .build();

            TrafficDetails trafficDetails = new TrafficDetails(trafficYoutube, 4, 2);
            TrafficDetails expiredTraffic = new TrafficDetails(trafficYoutube, 5, -1);
            TrafficDetails trafficDetails2 = new TrafficDetails(trafficYoutube, 6, 3);

            SimCard simCard = SimCard.builder()
                    .isActive(true)
                    .number("89998887766")
                    .minutesDetails(Arrays.asList(packageDetails, packageDetails2, packageDetails3))
                    .trafficDetails(Arrays.asList(trafficDetails, expiredTraffic, trafficDetails2))
                    .build();

            packageDetails.setSimCard(simCard);
            packageDetails2.setSimCard(simCard);
            packageDetails3.setSimCard(simCard);

            trafficDetails.setSimCard(simCard);
            expiredTraffic.setSimCard(simCard);
            trafficDetails2.setSimCard(simCard);

            minutesRepository.saveAll(Arrays.asList(favoriteNumber, freeRoaming));
            trafficRepository.saveAll(Arrays.asList(trafficYoutube));
            simCardRepository.saveAndFlush(simCard);
        };
    }
}
