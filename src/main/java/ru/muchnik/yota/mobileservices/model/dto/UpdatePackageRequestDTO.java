package ru.muchnik.yota.mobileservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for update package operation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePackageRequestDTO {
    private String basePackageId;
    private int addition;
    private int daysToLive;
}
