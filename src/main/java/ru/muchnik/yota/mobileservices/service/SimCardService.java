package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficPackageService;

@Service
@RequiredArgsConstructor
public class SimCardService {
    private final SimCardRepository simCardRepository;
    private final MinutesPackageService minutesPackageService;
    private final TrafficPackageService trafficPackageService;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public SimCard getSimCard(@NonNull final String number) {
        return simCardRepository.getByNumber(number).orElseThrow(() -> new NotFoundException("Sim Card is not found!"));
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public boolean getSimCardStatus(@NonNull final String number) {
        return getSimCard(number).isActive();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SimCard updateSimCardStatus(@NonNull final String number, final boolean status) {
        SimCard simCard = getSimCard(number);
        simCard.setActive(status);
        return simCard;
    }

    /**
     * Adds a minutes-package to specified sim card by number
     *
     * @param number        target sim card number
     * @param basePackageId base package id
     * @param minutes       minutes amount to add (can be negative to decrement)
     * @param daysToLive    days to live
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MinutesDetails addPackageOfMinutesToSimCard(@NonNull final String number,
                                                       @NonNull final String basePackageId,
                                                       final int minutes,
                                                       final int daysToLive) {
        final SimCard simCard = getSimCard(number);
        final MinutesPackageCatalog basePackage = minutesPackageService.getPackage(basePackageId);
        final MinutesDetails details = new MinutesDetails(basePackage, minutes, daysToLive);
        details.setSimCard(simCard);
        simCard.getMinutesDetails().add(details);
        return details;
    }

    /**
     * Adds a traffic-package to specified sim card by number
     *
     * @param number        target sim card number
     * @param basePackageId base package id
     * @param traffic       traffic amount to add (can be negative to decrement)
     * @param daysToLive    days to live
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TrafficDetails addTrafficPackageToSimCard(@NonNull final String number,
                                                     @NonNull final String basePackageId,
                                                     final int traffic,
                                                     final int daysToLive) {
        final SimCard simCard = getSimCard(number);
        final TrafficPackageCatalog basePackage = trafficPackageService.getPackage(basePackageId);
        final TrafficDetails details = new TrafficDetails(basePackage, traffic, daysToLive);
        details.setSimCard(simCard);
        simCard.getTrafficDetails().add(details);
        return details;
    }
}
