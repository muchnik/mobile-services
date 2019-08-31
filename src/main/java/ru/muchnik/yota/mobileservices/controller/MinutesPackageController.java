package ru.muchnik.yota.mobileservices.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.muchnik.yota.mobileservices.model.dto.CreatePackageDTO;
import ru.muchnik.yota.mobileservices.model.entity.IPackageCatalog;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import java.net.URI;

@Controller
@RequestMapping(value = "api/v1/packages-of-minutes/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MinutesPackageController extends BasePackageController<MinutesPackageCatalog, MinutesDetails, MinutesPackageService, MinutesDetailsService> {
    public MinutesPackageController(final MinutesPackageService packageService, final MinutesDetailsService detailsService) {
        super(packageService, detailsService);
    }

    @Override
    @PostMapping
    public ResponseEntity<MinutesPackageCatalog> savePackage(@RequestBody final MinutesPackageCatalog packageToSave) {
        MinutesPackageCatalog savedPackage = packageService.savePackage(packageToSave);
        return ResponseEntity.created(URI.create("/api/v1/packages-of-minutes/" + savedPackage.getId()))
                .body(savedPackage);
    }
}
