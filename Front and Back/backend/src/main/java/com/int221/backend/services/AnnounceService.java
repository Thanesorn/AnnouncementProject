package com.int221.backend.services;

import com.int221.backend.dto.CategoryDto;
import com.int221.backend.entities.Announce;
import com.int221.backend.entities.Category;
import com.int221.backend.mapper.ListMapper;
import com.int221.backend.repositories.AnnounceRepository;
import com.int221.backend.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnnounceService {
    @Autowired
    private AnnounceRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private CategoryService categoryService;

    public List<Announce> getAllAnnounce() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "publishDate", "closeDate"));
    }

    public Announce getAnnounce(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id" + " does not exist")
        );
    }


    public Announce addAnnounce(Announce newAnnounce) {
        return repository.saveAndFlush(newAnnounce);
    }

    public void deleteAnnounce(Integer id) {
        Optional<Announce> announce = repository.findById(id);
        if (!announce.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announce not found");
        }
        repository.deleteById(id);
    }


    public Announce updateAnnounce(Integer id, Announce announce) {
        Announce newAnnounce = repository.findById(id).orElseThrow(() ->
                new ResourceAccessException(id + " does not exist "));
        newAnnounce.setAnnouncementTitle(announce.getAnnouncementTitle());
        newAnnounce.setAnnouncementDescription(announce.getAnnouncementDescription());
        newAnnounce.setPublishDate(announce.getPublishDate());
        newAnnounce.setCloseDate(announce.getCloseDate());
        newAnnounce.setAnnouncementDisplay(announce.getAnnouncementDisplay());
        if (announce.getAnnouncementCategory() != null) {
            newAnnounce.setAnnouncementCategory(announce.getAnnouncementCategory());
        }
        return repository.saveAndFlush(newAnnounce);
    }



    public List<Announce> getAnnouncementByMode(String mode) {
        if (mode.equals("active")) {
            List<Announce> announces = new ArrayList<>();
            announces.addAll(repository.findAllAnnounceByDisplay("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayAndDate("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayWithoutDate("Y"));
            announces.addAll(repository.findAllAnnounceByDisplayAndDateWithoutCategory("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));

            Comparator<Announce> byPublishDateAndCloseDate = Comparator.comparing((Announce a) -> a.getPublishDate() != null)
                    .thenComparing((Announce a) -> a.getCloseDate() != null)
                    .thenComparing(Announce::getPublishDate, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(Announce::getCloseDate, Comparator.nullsFirst(Comparator.reverseOrder()));

            announces.sort(byPublishDateAndCloseDate);
            return announces;
        } else if (mode.equals("close")) {
            return repository.findAllAnnounceByModeClose("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()),Sort.by(Sort.Direction.DESC, "publishDate", "closeDate"));
        } else {
            return getAllAnnounce();
        }
    }




    public AnnounceService(AnnounceRepository repository) {
        this.repository = repository;
    }






    public Page<Announce> getPageAnnounceCategory(int page, int size, String mode, CategoryDto category,Integer categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate", "closeDate"));

        if (mode.equals("active")) {
            List<Announce> announces = new ArrayList<>();
            announces.addAll(repository.findAllAnnounceByDisplay("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayAndDate("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayWithoutDate("Y"));
            announces.addAll(repository.findAllAnnounceByDisplayAndDateWithoutCategory("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));

            CategoryDto categoryDto = category;
            if (categoryId != 0) {
                List<Announce> filteredAnnounces = new ArrayList<>();
                for (Announce announce : announces) {
                    if (announce.getAnnouncementCategory() != null && announce.getAnnouncementCategory().getCategoryName().equals(categoryDto.getCategoryName())) {
                        filteredAnnounces.add(announce);
                    }
                }
                announces = filteredAnnounces;
            }
            Comparator<Announce> byPublishDateAndCloseDate = Comparator.comparing((Announce a) -> a.getPublishDate() != null && a.getCloseDate() != null)
                    .thenComparing(Announce::getPublishDate, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(Announce::getCloseDate, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(Announce::getId, Comparator.reverseOrder());

            announces.sort(byPublishDateAndCloseDate);

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), announces.size());
            return new PageImpl<>(announces.subList(start, end), pageable, announces.size());
        } else if (mode.equals("close")) {
            List<Announce> announces = new ArrayList<>();
            announces.addAll(repository.findAllAnnounceByModeClose("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));

            CategoryDto categoryDto = category;
            if (categoryId != 0) {
                List<Announce> filteredAnnounces = new ArrayList<>();
                for (Announce announce : announces) {
                    if (announce.getAnnouncementCategory() != null && announce.getAnnouncementCategory().getCategoryName().equals(categoryDto.getCategoryName())) {
                        filteredAnnounces.add(announce);
                    }
                }
                announces = filteredAnnounces;
            }else if (categoryId == 0){
                List<Announce> announce = new ArrayList<>();
                announce.addAll(repository.findAllAnnounceByModeClose("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));
                announces = announce;
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), announces.size());
            return new PageImpl<>(announces.subList(start, end), pageable, announces.size());
        }
        else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }



    }

    public Page<Announce> getPageAnnounce(int page, int size, String mode) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate", "closeDate"));

        if (mode.equals("active")) {
            List<Announce> announces = new ArrayList<>();
            announces.addAll(repository.findAllAnnounceByDisplay("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayAndDate("Y", ZonedDateTime.parse(ZonedDateTime.now().toString())));
            announces.addAll(repository.findAllAnnounceByDisplayWithoutDate("Y"));
            announces.addAll(repository.findAllAnnounceByDisplayAndDateWithoutCategory("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));

            Comparator<Announce> byPublishDateAndCloseDate = Comparator.comparing((Announce a) -> a.getPublishDate() != null && a.getCloseDate() != null)
                    .thenComparing(Announce::getPublishDate, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(Announce::getCloseDate, Comparator.nullsFirst(Comparator.reverseOrder()))
                    .thenComparing(Announce::getId, Comparator.reverseOrder());

            announces.sort(byPublishDateAndCloseDate);

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), announces.size());
            return new PageImpl<>(announces.subList(start, end), pageable, announces.size());
        } else if (mode.equals("close")) {
            List<Announce> announces = new ArrayList<>();
            announces.addAll(repository.findAllAnnounceByModeClose("Y", ZonedDateTime.parse(ZonedDateTime.now().toString()), Sort.by(Sort.Direction.DESC, "publishDate", "closeDate")));



            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), announces.size());
            return new PageImpl<>(announces.subList(start, end), pageable, announces.size());


        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }

    }





}
