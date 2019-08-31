package ru.muchnik.yota.mobileservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;

import java.util.Optional;

@Repository
public interface SimCardRepository extends JpaRepository<SimCard, String> {
    Optional<SimCard> getByNumber(String number);
}
