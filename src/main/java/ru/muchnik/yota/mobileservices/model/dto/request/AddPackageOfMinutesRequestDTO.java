package ru.muchnik.yota.mobileservices.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPackageOfMinutesRequestDTO {
    private String name;
    private PackageOfMinutes.PackageOfMinutesType type;
}
