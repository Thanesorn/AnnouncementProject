package com.int221.backend.repositories;

import com.int221.backend.entities.Announce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announce, Integer> {


    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND (a.publishDate IS NULL AND a.closeDate IS NULL) " )
    List<Announce> findAllAnnounceByDisplayWithoutDate(String display);


    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND (a.publishDate <= :currentDate AND a.closeDate IS NULL) " +
            "AND a.announcementDisplay = 'Y'")
    List <Announce> findAllAnnounceByDisplay(String display, ZonedDateTime currentDate);


    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND (a.publishDate <= :currentDate AND a.closeDate > :currentDate) " +
            "AND a.announcementDisplay = 'Y'")
    List<Announce> findAllAnnounceByDisplayAndDate(String display, ZonedDateTime currentDate);

    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND (a.closeDate > :currentDate AND a.publishDate IS NULL) " +
            "AND a.announcementDisplay = 'Y'")
    List<Announce> findAllAnnounceByDisplayAndDateWithoutCategory(String display, ZonedDateTime currentDate, Sort by);

    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND a.closeDate <= :currentDate " +
            "AND a.announcementDisplay = 'Y'")
    List<Announce> findAllAnnounceByModeClose(String display, ZonedDateTime currentDate, Sort by);


    @Query("SELECT a FROM Announce a WHERE a.announcementDisplay = :display " +
            "AND a.closeDate <= :currentDate " +
            "AND a.announcementDisplay = 'Y'")
    Page<Announce> findAllAnnounceByPageClose(String display, ZonedDateTime currentDate, Pageable pageable);



}