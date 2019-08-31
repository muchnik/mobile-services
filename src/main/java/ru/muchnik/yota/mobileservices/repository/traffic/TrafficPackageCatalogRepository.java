package ru.muchnik.yota.mobileservices.repository.traffic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;

@Repository
public interface TrafficPackageCatalogRepository extends JpaRepository<TrafficPackageCatalog, String> {
}
