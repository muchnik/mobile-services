package ru.muchnik.yota.mobileservices.model.entity.minutes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.muchnik.yota.mobileservices.model.entity.IDetails;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;

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
public class MinutesDetails implements IDetails<MinutesPackageCatalog> {
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
    private MinutesPackageCatalog basePackage;

    /**
     * Reference to package owner sim card
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "sim_card_id")
    private SimCard simCard;

    @PositiveOrZero
    private int minutesLeft;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime activationDate;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expirationDate;

    public MinutesDetails(@NotNull final MinutesPackageCatalog basePackage, final int minutes, final int daysToLive) {
        LocalDateTime activationDate = LocalDateTime.now();
        this.basePackage = basePackage;
        this.activationDate = activationDate;
        this.minutesLeft = minutes;
        this.expirationDate = activationDate.plusDays(daysToLive);
    }

    @Override
    public int getAmountLeft() {
        return getMinutesLeft();
    }

    @Override
    public void setAmountLeft(final int amountLeft) {
        setMinutesLeft(amountLeft);
    }
}
