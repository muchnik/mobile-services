package ru.muchnik.yota.mobileservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.SimCard;

import java.util.Optional;

@Repository
public interface SimCardRepository extends JpaRepository<SimCard, Long> {
    Optional<SimCard> getByNumber(String number);

    @Query("update SimCard sc set sc.isActive = :status where sc.number = :number")
    @Modifying
    Integer updateStatus(@Param("number") String number, @Param("status") boolean status);
}
