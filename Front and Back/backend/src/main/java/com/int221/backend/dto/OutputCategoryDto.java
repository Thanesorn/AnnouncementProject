package com.int221.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class OutputCategoryDto {
    private int id;
    private String categoryName;
}
