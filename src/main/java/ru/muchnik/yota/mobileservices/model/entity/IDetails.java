package ru.muchnik.yota.mobileservices.model.entity;

import java.time.LocalDateTime;

public interface IDetails<BasePackage extends IPackageCatalog> {
    String getId();

    BasePackage getBasePackage();

    SimCard getSimCard();

    int getAmountLeft();

    LocalDateTime getActivationDate();

    LocalDateTime getExpirationDate();

    void setBasePackage(BasePackage basePackage);

    void setSimCard(SimCard simCard);

    void setAmountLeft(int amountLeft);

    void setActivationDate(java.time.LocalDateTime activationDate);

    void setExpirationDate(java.time.LocalDateTime expirationDate);
}
