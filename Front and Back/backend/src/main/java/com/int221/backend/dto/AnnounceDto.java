package com.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.int221.backend.entities.Announce} entity
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class AnnounceDto implements Serializable {

    private  Integer id;

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 200, message = "size must be between 1 and 200")
    private  String announcementTitle;

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 10000, message = "size must be between 1 and 10000")
    private  String announcementDescription;
    private ZonedDateTime publishDate;
    private  ZonedDateTime closeDate;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[YN]$", message = "must be either 'Y' or 'N'")
    private  String announcementDisplay;

    @NotNull(message = "must not be null")
    @Min(value = 1, message = "does not exists")
    @Max(value = 4, message = "does not exists")
    private Integer categoryId;

}