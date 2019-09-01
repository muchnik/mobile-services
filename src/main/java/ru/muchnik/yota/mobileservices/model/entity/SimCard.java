package ru.muchnik.yota.mobileservices.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SimCard {
    @Id
    @EqualsAndHashCode.Include
    @Size(max = 36)
    private final String id = UUID.randomUUID().toString();

    @Column(unique = true)
    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "[0-9]+")
    private String number;

    /**
     * Sim card can be in two statuses: active(true), deactivated(false)
     */
    private boolean isActive;

    /**
     * All {@link MinutesDetails} packages of minutes that is attached to this sim card
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard", fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    @ToString.Exclude
    private List<MinutesDetails> minutesDetails;

    /**
     * All {@link MinutesDetails} packages of traffic that is attached to this sim card
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "simCard", fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    @ToString.Exclude
    private List<TrafficDetails> trafficDetails;
}
