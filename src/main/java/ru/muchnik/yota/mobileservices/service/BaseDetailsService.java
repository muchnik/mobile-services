package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.IDetails;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.model.exception.ValidationException;
import ru.muchnik.yota.mobileservices.repository.BaseDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseDetailsService<Type extends IDetails, Repository extends BaseDetailsRepository<Type> & JpaRepository<Type, String>> {
    private final Repository repository;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Type getDetails(@NonNull final String id) {
        Optional<Type> detailsOptional = repository.findById(id);
        if (!detailsOptional.isPresent()) throw new NotFoundException("Details for package of traffic not found");
        return detailsOptional.get();
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateAmount(@NonNull final String detailsId, final int addition) {
        Type details = getDetails(detailsId);
        doValidate(details, addition);
        int amountLeft = details.getAmountLeft();
        amountLeft += addition;
        details.setAmountLeft(amountLeft);
    }
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Type> getAllActivePackages(@NonNull final String number) {
        return repository.findAllActive(number, LocalDateTime.now());
    }

    private void doValidate(@NonNull final Type details, final int addition) {
        doValidateExpiration(details);
        doValidateAdjusting(details, addition);
    }

    private void doValidateExpiration(@NonNull final Type details) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activationDate = details.getActivationDate();
        LocalDateTime expirationDate = details.getExpirationDate();

        if (!activationDate.isBefore(now) || !expirationDate.isAfter(now)) {
            throw new ValidationException("Package has expired!");
        }
    }

    private void doValidateAdjusting(@NonNull final Type details, final int addition) {
        int amountLeft = details.getAmountLeft();
        if (addition < 0 && amountLeft < (-addition)) {
            throw new ValidationException("Amount in package of addition cannot be decremented! Left:" + amountLeft + " Decrement:" + addition);
        }
    }
}
