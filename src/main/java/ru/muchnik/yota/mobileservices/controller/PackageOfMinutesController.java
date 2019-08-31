package ru.muchnik.yota.mobileservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.muchnik.yota.mobileservices.model.dto.UnitDTO;
import ru.muchnik.yota.mobileservices.model.dto.request.AddPackageOfMinutesRequestDTO;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.service.MinutesPackageDetailsService;
import ru.muchnik.yota.mobileservices.service.PackageOfMinutesService;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/package-of-minutes/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PackageOfMinutesController {
    private final PackageOfMinutesService packageService;
    private final MinutesPackageDetailsService detailsService;

    @GetMapping("{packageId}")
    public ResponseEntity<PackageOfMinutes> getPackage(@PathVariable long packageId) {
        return ResponseEntity.ok(packageService.getPackage(packageId));
    }

    @PostMapping
    public ResponseEntity<PackageOfMinutes> savePackage(@RequestBody AddPackageOfMinutesRequestDTO dto) {
        PackageOfMinutes mp = PackageOfMinutes.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
        PackageOfMinutes savedPackage = packageService.savePackage(mp);
        return ResponseEntity.created(URI.create("/api/v1/package-of-minutes/" + savedPackage.getId()))
                .body(savedPackage);
    }

    @PutMapping("/details/{detailsId}")
    public ResponseEntity<Void> updateMinutes(@PathVariable long detailsId, @RequestBody UnitDTO<Integer> input) {
        detailsService.updateMinutes(detailsId, input.getValue());
        return ResponseEntity.ok().build();
    }
}
