package ru.muchnik.yota.mobileservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.muchnik.yota.mobileservices.model.entity.IPackageCatalog;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BasePackageService<Type extends IPackageCatalog, Repository extends JpaRepository<Type, String>> {
    private final Repository repository;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Type getPackage(@NonNull final String id) {
        Optional<Type> optionalPackage = repository.findById(id);
        if (!optionalPackage.isPresent()) throw new NotFoundException("Package not found");
        return optionalPackage.get();
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Type> getPackages() {
        return repository.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Type savePackage(@NonNull Type basePackageOfMinutes) {
        return repository.save(basePackageOfMinutes);
    }
}
