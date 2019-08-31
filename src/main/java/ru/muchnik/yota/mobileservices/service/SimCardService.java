package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.MinutesPackageDetailsRepository;
import ru.muchnik.yota.mobileservices.repository.SimCardRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimCardService {
    private final SimCardRepository simCardRepository;
    private final PackageOfMinutesService packageOfMinutesService;
    private final MinutesPackageDetailsRepository detailsRepository;

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
    public boolean updateSimCardStatus(@NonNull final String number, final boolean status) {
        return simCardRepository.updateStatus(number, status) > 0;
    }

    /**
     * Adds a minutes package to specified sim card by number
     *
     * @param number        target sim card number
     * @param basePackageId base package id
     * @param minutes       minutes to add
     * @param daysToLive    days to live
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MinutesPackageDetails addPackageOfMinutesToSimCard(@NonNull final String number,
                                                              final long basePackageId,
                                                              final int minutes,
                                                              final int daysToLive) {
        final SimCard simCard = getSimCard(number);
        final PackageOfMinutes basePackage = packageOfMinutesService.getPackage(basePackageId);
        final MinutesPackageDetails details = new MinutesPackageDetails(basePackage, minutes, daysToLive);
        details.setSimCard(simCard);
        simCard.getMinutesPackageDetails().add(details);
        return detailsRepository.save(details);
    }
}
