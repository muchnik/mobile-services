package ru.muchnik.yota.mobileservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.muchnik.yota.mobileservices.model.dto.UnitDTO;
import ru.muchnik.yota.mobileservices.model.dto.request.AddMinutesToPackageOfMinutesRequestDTO;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.service.MinutesPackageDetailsService;
import ru.muchnik.yota.mobileservices.service.SimCardService;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/sim-card/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SimCardController {
    private final SimCardService simCardService;
    private final MinutesPackageDetailsService detailsService;

    @GetMapping("{number}")
    public ResponseEntity<SimCard> getSimCard(@PathVariable String number) {
        return ResponseEntity.ok(simCardService.getSimCard(number));
    }

    @GetMapping("{number}/status")
    public ResponseEntity<UnitDTO<Boolean>> getSimCardStatus(@PathVariable String number) {
        return ResponseEntity.ok(new UnitDTO<>(simCardService.getSimCardStatus(number)));
    }

    @PutMapping("{number}/status")
    public ResponseEntity<UnitDTO<Boolean>> updateSimCardStatus(@PathVariable String number,
                                                                @RequestBody UnitDTO<Boolean> statusDTO) {
        boolean updated = simCardService.updateSimCardStatus(number, statusDTO.getValue());
        return ResponseEntity.ok(new UnitDTO<>(updated));
    }

    @GetMapping("{number}/minutes/total")
    public ResponseEntity<UnitDTO<Integer>> getSimCardActiveMinutesTotal(@PathVariable String number) {
        int sumOfMinutesLeft = detailsService.getAllActivePackages(number).stream()
                .mapToInt(MinutesPackageDetails::getMinutesLeft)
                .sum();
        return ResponseEntity.ok(new UnitDTO<>(sumOfMinutesLeft));
    }

    @GetMapping("{number}/minutes/packages")
    public ResponseEntity<List<MinutesPackageDetails>> getSimCardActivePackagesOfMinutes(@PathVariable String number) {
        return ResponseEntity.ok(detailsService.getAllActivePackages(number));
    }

    @PostMapping("{number}/minutes")
    public ResponseEntity<MinutesPackageDetails> addPackageOfMinutesToSimCard(@PathVariable String number,
                                                                              @RequestBody AddMinutesToPackageOfMinutesRequestDTO requestDTO) {
        MinutesPackageDetails savedDetails = simCardService.addPackageOfMinutesToSimCard(number, requestDTO.getBasePackageId(), requestDTO.getMinutes(), requestDTO.getDaysToLive());
        return ResponseEntity.created(URI.create("/api/v1/package-of-minutes/" + savedDetails.getId()))
                .body(savedDetails);
    }
}
