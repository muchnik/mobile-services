package ru.muchnik.yota.mobileservices.service.traffic;

import org.springframework.stereotype.Service;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.repository.traffic.TrafficPackageCatalogRepository;
import ru.muchnik.yota.mobileservices.service.BasePackageService;

@Service
public class TrafficPackageService extends BasePackageService<TrafficPackageCatalog, TrafficPackageCatalogRepository> {
    public TrafficPackageService(final TrafficPackageCatalogRepository repository) {
        super(repository);
    }
}
