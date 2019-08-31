package ru.muchnik.yota.mobileservices.model.entity;

public interface IPackageCatalog<Type> {
    String getId();

    String getName();

    Type getType();

    void setName(String name);

    void setType(Type type);
}
