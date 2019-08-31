package ru.muchnik.yota.mobileservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple DTO for single value-container
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitDTO<T> {
    private T value;
}
