package ru.muchnik.yota.mobileservices.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.muchnik.yota.mobileservices.model.dto.CreatePackageDTO;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficDetailsService;
import ru.muchnik.yota.mobileservices.service.traffic.TrafficPackageService;

import java.net.URI;

@Controller
@RequestMapping(value = "api/v1/traffic-packages/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TrafficPackageController extends BasePackageController<TrafficPackageCatalog, TrafficDetails, TrafficPackageService, TrafficDetailsService>  {
    public TrafficPackageController(final TrafficPackageService packageService, final TrafficDetailsService detailsService) {
        super(packageService, detailsService);
    }

    @Override
    @PostMapping
    public ResponseEntity<TrafficPackageCatalog> savePackage(@RequestBody final TrafficPackageCatalog packageToSave) {
        TrafficPackageCatalog savedPackage = packageService.savePackage(packageToSave);
        return ResponseEntity.created(URI.create("/api/v1/traffic-packages/" + savedPackage.getId()))
                .body(savedPackage);
    }
}

