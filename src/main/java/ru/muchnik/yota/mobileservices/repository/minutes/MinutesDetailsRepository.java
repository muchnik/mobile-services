package ru.muchnik.yota.mobileservices.repository.minutes;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.repository.BaseDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinutesDetailsRepository extends BaseDetailsRepository<MinutesDetails> {
    @Query("select d from MinutesDetails d " +
            "join d.simCard " +
            "where number = :number " +
            "and :date between d.activationDate and d.expirationDate")
    @EntityGraph(attributePaths = {"simCard", "basePackage"})
    @Override
    List<MinutesDetails> findAllActive(@Param("number") String number, @Param("date") LocalDateTime now);
}
