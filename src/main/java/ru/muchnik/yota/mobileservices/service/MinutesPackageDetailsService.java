package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;
import ru.muchnik.yota.mobileservices.repository.MinutesPackageDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MinutesPackageDetailsService {
    private final MinutesPackageDetailsRepository repository;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public MinutesPackageDetails getDetails(final long id) {
        Optional<MinutesPackageDetails> detailsOptional = repository.findById(id);
        if (!detailsOptional.isPresent()) throw new NotFoundException("Details for package of minutes not found");
        return detailsOptional.get();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateMinutes(final long detailsId, final int adjustMinutes) {
        MinutesPackageDetails details = getDetails(detailsId);
        doValidate(details, adjustMinutes);
        int minutesLeft = details.getMinutesLeft();
        minutesLeft += adjustMinutes;
        details.setMinutesLeft(minutesLeft);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<MinutesPackageDetails> getAllActivePackages(@NonNull final String number) {
        return repository.findAllActive(number, LocalDateTime.now());
    }

    private void doValidate(@NonNull final MinutesPackageDetails details, final int adjustMinutes) {
        doValidateExpiration(details);
        doValidateAdjusting(details, adjustMinutes);
    }

    private void doValidateExpiration(@NonNull final MinutesPackageDetails details) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activationDate = details.getActivationDate();
        LocalDateTime expirationDate = details.getExpirationDate();

        if (!activationDate.isBefore(now) || !expirationDate.isAfter(now)) {
            throw new ValidationException("Minute Package has expired!");
        }
    }

    private void doValidateAdjusting(@NonNull final MinutesPackageDetails details, final int adjustMinutes) {
        int minutesLeft = details.getMinutesLeft();
        if (adjustMinutes < 0 && minutesLeft < (-adjustMinutes)) {
            throw new ValidationException("Minutes in package of minutes cannot be decremented! Left:" + minutesLeft + " Decrement:" + adjustMinutes);
        }
    }
}
