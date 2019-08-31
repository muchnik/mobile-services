package ru.muchnik.yota.mobileservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;

@Repository
public interface PackageOfMinutesRepository extends JpaRepository<PackageOfMinutes, Long> {
}
