package ru.muchnik.yota.mobileservices.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple DTO for single value-container
 * @param <Type> type of value
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueDTO<Type> {
    private Type value;
}
