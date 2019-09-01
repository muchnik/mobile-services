package ru.muchnik.yota.mobileservices.model.entity.traffic;


import lombok.*;
import ru.muchnik.yota.mobileservices.model.entity.IPackageCatalog;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrafficPackageCatalog implements IPackageCatalog<TrafficPackageCatalog.Type> {
    @Id
    @EqualsAndHashCode.Include
    @Size(max = 36)
    private final String id = UUID.randomUUID().toString();

    /**
     * Package business name
     */
    @NotBlank
    private String name;

    /**
     * Type, in future may contains relations with other entities that contains package behaviour
     */
    @Enumerated
    private Type type;

    public enum Type {
        FREE_MESSENGER, YOUTUBE
    }
}
