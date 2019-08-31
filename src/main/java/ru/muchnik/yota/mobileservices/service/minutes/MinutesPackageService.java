package ru.muchnik.yota.mobileservices.service.minutes;

import org.springframework.stereotype.Service;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesPackageCatalogRepository;
import ru.muchnik.yota.mobileservices.service.BasePackageService;

@Service
public class MinutesPackageService extends BasePackageService<MinutesPackageCatalog, MinutesPackageCatalogRepository> {
    public MinutesPackageService(final MinutesPackageCatalogRepository repository) {
        super(repository);
    }
}
