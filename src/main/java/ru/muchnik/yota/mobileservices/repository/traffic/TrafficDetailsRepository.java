package ru.muchnik.yota.mobileservices.repository.traffic;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.repository.BaseDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrafficDetailsRepository extends BaseDetailsRepository<TrafficDetails> {
    @Query("select t from TrafficDetails t " +
            "join t.simCard " +
            "where number = :number " +
            "and :date between t.activationDate and t.expirationDate")
    @EntityGraph(attributePaths = {"simCard", "basePackage"})
    @Override
    List<TrafficDetails> findAllActive(@Param("number") String number, @Param("date") LocalDateTime now);
}
