package ru.muchnik.yota.mobileservices.repository.minutes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;

@Repository
public interface MinutesPackageCatalogRepository extends JpaRepository<MinutesPackageCatalog, String> {
}
