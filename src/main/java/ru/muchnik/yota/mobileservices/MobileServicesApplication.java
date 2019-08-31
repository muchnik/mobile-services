package ru.muchnik.yota.mobileservices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.repository.PackageOfMinutesRepository;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;

import java.util.Arrays;

/**
 * Application entry point
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebMvc
public class MobileServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MobileServicesApplication.class, args);
    }

    /**
     * Filling database with test data, when application starts
     *
     * @apiNote <b>Just for testing purposes</b>
     */
    @Bean
    public CommandLineRunner runner(SimCardRepository simCardRepository, PackageOfMinutesRepository packageRepository) {
        return args -> {
            PackageOfMinutes freeRoaming = PackageOfMinutes.builder()
                    .name("FreeRoaming")
                    .type(PackageOfMinutes.PackageOfMinutesType.FREE_ROAMING)
                    .build();

            PackageOfMinutes favoriteNumber = PackageOfMinutes.builder()
                    .name("FavoriteNumber")
                    .type(PackageOfMinutes.PackageOfMinutesType.FAVORITE_NUMBER)
                    .build();

            MinutesPackageDetails packageDetails = new MinutesPackageDetails(freeRoaming, 300, 30);
            MinutesPackageDetails packageDetails2 = new MinutesPackageDetails(freeRoaming, 200, -1);
            MinutesPackageDetails packageDetails3 = new MinutesPackageDetails(freeRoaming, 100, 1);

            SimCard simCard = SimCard.builder()
                    .isActive(true)
                    .number("89998887766")
                    .minutesPackageDetails(Arrays.asList(packageDetails, packageDetails2, packageDetails3))
                    .build();

            packageDetails.setSimCard(simCard);
            packageDetails2.setSimCard(simCard);
            packageDetails3.setSimCard(simCard);

            packageRepository.saveAll(Arrays.asList(favoriteNumber, freeRoaming));
            simCardRepository.saveAndFlush(simCard);
        };
    }
}
