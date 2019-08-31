package ru.muchnik.yota.mobileservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface BaseDetailsRepository<Type> extends JpaRepository<Type, String> {
    List<Type> findAllActive(@Param("number") String number, @Param("date") LocalDateTime now);
}
