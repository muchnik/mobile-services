package ru.muchnik.yota.mobileservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.muchnik.yota.mobileservices.model.dto.ValueDTO;
import ru.muchnik.yota.mobileservices.model.entity.IDetails;
import ru.muchnik.yota.mobileservices.model.entity.IPackageCatalog;
import ru.muchnik.yota.mobileservices.repository.BaseDetailsRepository;
import ru.muchnik.yota.mobileservices.service.BaseDetailsService;
import ru.muchnik.yota.mobileservices.service.BasePackageService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class BasePackageController<Type extends IPackageCatalog,
        Details extends IDetails,
        PackageService extends BasePackageService<Type, ? extends JpaRepository<Type, String>>,
        DetailsService extends BaseDetailsService<Details, ? extends BaseDetailsRepository<Details>>> {
    protected final PackageService packageService;
    protected final DetailsService detailsService;

    @GetMapping("{packageId}")
    public ResponseEntity<Type> getPackage(@PathVariable String packageId) {
        return ResponseEntity.ok(packageService.getPackage(packageId));
    }

    @GetMapping
    public ResponseEntity<List<Type>> getPackages() {
        return ResponseEntity.ok(packageService.getPackages());
    }

    @PostMapping
    public abstract ResponseEntity<Type> savePackage(@RequestBody Type packageToSave);

    @PutMapping("/details/{detailsId}")
    public ResponseEntity<Void> updateAmount(@PathVariable String detailsId, @RequestBody ValueDTO<Integer> input) {
        Integer value = input.getValue();
        if (value != 0) {
            detailsService.updateAmount(detailsId, value);
        }
        return ResponseEntity.ok().build();
    }
}
