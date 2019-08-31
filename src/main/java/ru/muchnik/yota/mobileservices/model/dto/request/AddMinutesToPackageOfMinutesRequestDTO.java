package ru.muchnik.yota.mobileservices.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMinutesToPackageOfMinutesRequestDTO {
    private long basePackageId;
    private int minutes;
    private int daysToLive;
}
