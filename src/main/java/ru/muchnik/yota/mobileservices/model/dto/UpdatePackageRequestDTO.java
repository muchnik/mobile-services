package ru.muchnik.yota.mobileservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePackageRequestDTO {
    private String basePackageId;
    private int addition;
    private int daysToLive;
}
