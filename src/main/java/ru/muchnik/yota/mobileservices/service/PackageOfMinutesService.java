package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.PackageOfMinutesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageOfMinutesService {
    private final PackageOfMinutesRepository repository;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public PackageOfMinutes getPackage(final long id) {
        Optional<PackageOfMinutes> optionalMinutesPackage = repository.findById(id);
        if (!optionalMinutesPackage.isPresent()) throw new NotFoundException("Package of minutes not found");
        return optionalMinutesPackage.get();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PackageOfMinutes savePackage(@NonNull PackageOfMinutes packageOfMinutes) {
        return repository.save(packageOfMinutes);
    }
}
