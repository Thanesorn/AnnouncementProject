package com.int221.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link com.int221.backend.entities.Category} entity
 */
@Getter
@Setter
@Data
public class CategoryDto implements Serializable {
    private String categoryName;
}