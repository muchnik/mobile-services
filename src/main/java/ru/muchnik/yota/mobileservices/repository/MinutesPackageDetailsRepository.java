package ru.muchnik.yota.mobileservices.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.MinutesPackageDetails;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinutesPackageDetailsRepository extends JpaRepository<MinutesPackageDetails, Long> {
    @Query("select d from MinutesPackageDetails d " +
            "join d.simCard " +
            "where number = :number " +
            "and :date between d.activationDate and d.expirationDate")
    @EntityGraph(attributePaths = {"simCard", "basePackage"})
    List<MinutesPackageDetails> findAllActive(@Param("number") String number, @Param("date") LocalDateTime now);
}
