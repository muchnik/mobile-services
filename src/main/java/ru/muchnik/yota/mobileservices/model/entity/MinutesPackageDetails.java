package ru.muchnik.yota.mobileservices.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MinutesPackageDetails {
    @Id
    @EqualsAndHashCode.Include
    @Size(max = 36)
    private final String id = UUID.randomUUID().toString();
    /**
     * Reference to base package that may (in future) contains more business information
     */
    @ManyToOne(cascade = {}, optional = false)
    @NotNull
    @JoinColumn(name = "base_package_id")
    private PackageOfMinutes basePackage;
    /**
     * Reference to package owner sim card
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "sim_card_id")
    @JsonManagedReference
    private SimCard simCard;
    @PositiveOrZero
    private int minutesLeft;
    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime activationDate;
    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expirationDate;

    public MinutesPackageDetails(@NotNull final PackageOfMinutes basePackage, final int minutes, final int daysToLive) {
        LocalDateTime activationDate = LocalDateTime.now();
        this.basePackage = basePackage;
        this.activationDate = activationDate;
        this.minutesLeft = minutes;
        this.expirationDate = activationDate.plusDays(daysToLive);
    }
}
