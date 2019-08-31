package ru.muchnik.yota.mobileservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.muchnik.yota.mobileservices.model.dto.UpdatePackageRequestDTO;
import ru.muchnik.yota.mobileservices.model.dto.ValueDTO;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.service.SimCardService;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesDetailsService;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/sim-card/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SimCardController {
    private final SimCardService simCardService;
    private final MinutesDetailsService detailsService;

    @GetMapping("{number}")
    public ResponseEntity<SimCard> getSimCard(@PathVariable String number) {
        return ResponseEntity.ok(simCardService.getSimCard(number));
    }

    @GetMapping("{number}/status")
    public ResponseEntity<ValueDTO<Boolean>> getSimCardStatus(@PathVariable String number) {
        return ResponseEntity.ok(new ValueDTO<>(simCardService.getSimCardStatus(number)));
    }

    @PutMapping("{number}/status")
    public ResponseEntity<SimCard> updateSimCardStatus(@PathVariable String number,
                                                                @RequestBody ValueDTO<Boolean> statusDTO) {
        return ResponseEntity.ok(simCardService.updateSimCardStatus(number, statusDTO.getValue()));
    }

    @GetMapping("{number}/minutes/total")
    public ResponseEntity<ValueDTO<Integer>> getSimCardActiveMinutesTotal(@PathVariable String number) {
        int sumOfMinutesLeft = detailsService.getAllActivePackages(number).stream()
                .mapToInt(MinutesDetails::getMinutesLeft)
                .sum();
        return ResponseEntity.ok(new ValueDTO<>(sumOfMinutesLeft));
    }

    @GetMapping("{number}/minutes/packages")
    public ResponseEntity<List<MinutesDetails>> getSimCardActivePackagesOfMinutes(@PathVariable String number) {
        return ResponseEntity.ok(detailsService.getAllActivePackages(number));
    }

    @PostMapping("{number}/minutes")
    public ResponseEntity<MinutesDetails> addPackageOfMinutesToSimCard(@PathVariable String number,
                                                                       @RequestBody UpdatePackageRequestDTO requestDTO) {
        MinutesDetails savedDetails = simCardService.addPackageOfMinutesToSimCard(number, requestDTO.getBasePackageId(), requestDTO.getAddition(), requestDTO.getDaysToLive());
        return ResponseEntity.created(URI.create("/api/v1/packages-of-addition/" + savedDetails.getId()))
                .body(savedDetails);
    }
}
