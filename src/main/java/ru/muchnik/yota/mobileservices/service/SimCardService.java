package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesDetailsRepository;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimCardService {
    private final SimCardRepository simCardRepository;
    private final MinutesPackageService minutesPackageService;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public SimCard getSimCard(@NonNull final String number) {
        Optional<SimCard> optionalSimCard = simCardRepository.getByNumber(number);
        if (!optionalSimCard.isPresent()) throw new NotFoundException("Sim Card not found");
        return optionalSimCard.get();
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
     * Adds a addition package to specified sim card by number
     *
     * @param number        target sim card number
     * @param basePackageId base package id
     * @param minutes       addition to add
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
}
