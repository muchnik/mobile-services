package ru.muchnik.yota.mobileservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple DTO for creating package
 * @see ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog
 * @see ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficPackageCatalog
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePackageDTO {
    private String name;
    private String type;
}
