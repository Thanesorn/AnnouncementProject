package com.int221.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
public class OutputAnnouceDto  implements Serializable {
    private  Integer id;
    private  String announcementTitle;
    private  String announcementDescription;
    private ZonedDateTime publishDate;
    private  ZonedDateTime closeDate;
    private  String announcementDisplay;
    private  String announcementCategory;
}
