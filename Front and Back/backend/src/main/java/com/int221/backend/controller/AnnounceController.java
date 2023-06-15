package com.int221.backend.controller;

import ch.qos.logback.core.model.Model;
import com.int221.backend.dto.*;
import com.int221.backend.entities.Announce;
import com.int221.backend.entities.Category;
import com.int221.backend.mapper.ListMapper;
import com.int221.backend.repositories.CategoryRepository;
import com.int221.backend.services.AnnounceService;
import com.int221.backend.services.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Executable;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin
public class AnnounceController {
    @Autowired
    private AnnounceService service;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private CategoryService categoryService;

    private final AnnounceService announceService;


    @GetMapping("")
    public List<OutputAnnouceDto> getAnnouncementByMode(@RequestParam(defaultValue = "all") String mode) {
        List<Announce> announces = service.getAnnouncementByMode(mode);
        return listMapper.mapList(announces, OutputAnnouceDto.class, modelMapper);
    }




    @GetMapping("/{id}")
    public OutputAnnouceDto getAnnounce(@PathVariable Integer id) {
        return modelMapper.map(service.getAnnounce(id), OutputAnnouceDto.class);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public OutputAnnouceDto addAnnounceDtoBody(@Valid @RequestBody AnnounceDto newAnnounceDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            // ตรวจสอบข้อผิดพลาดที่เกิดจากการตรวจสอบความถูกต้องของอินพุต
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ValidationErrorDto> validationErrors = new ArrayList<>();

            for (FieldError fieldError : fieldErrors) {
                String field = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                ValidationErrorDto validationError = new ValidationErrorDto(field, errorMessage);
                validationErrors.add(validationError);
            }
            throw new MethodArgumentNotValidException((MethodParameter) null, bindingResult);


        }

        Announce announce = modelMapper.map(newAnnounceDto, Announce.class);
        announce.setId(null);
        announce.setAnnouncementCategory(categoryService.getCategoryById(newAnnounceDto.getCategoryId()));
        service.addAnnounce(announce);
        return modelMapper.map(announce, OutputAnnouceDto.class);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteAnnounce(id);
    }

    @PutMapping("/{id}")
    public OutputAnnouceDto updateAnnounce(@PathVariable Integer id, @RequestBody AnnounceDto newAnnounceDto) {
        Announce announce = modelMapper.map(newAnnounceDto, Announce.class);
        announce.setAnnouncementCategory(categoryService.getCategoryById(newAnnounceDto.getCategoryId()));
        service.updateAnnounce(id, announce);
        return modelMapper.map(announce, OutputAnnouceDto.class);
    }



    public AnnounceController(AnnounceService announceService) {
        this.announceService = announceService;
    }

    @GetMapping("/pages")
    public PageDTO<OutputAnnouceDto> getAllAnnounce(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "5") Integer size,
                                                    @RequestParam(defaultValue = "all") String mode,
                                                    @RequestParam(defaultValue = "0") Integer category) {
        
        if(category == 0){
            Page<Announce> announcePage = service.getPageAnnounce(page, size, mode);
            return listMapper.toPageDTO(announcePage, OutputAnnouceDto.class, modelMapper);
        }
        else{
            CategoryDto categories = modelMapper.map(categoryService.getCategoryById(category), CategoryDto.class);
            Page<Announce> announcePage = service.getPageAnnounceCategory(page, size, mode, categories, category);
            return listMapper.toPageDTO(announcePage, OutputAnnouceDto.class, modelMapper);
        }
    }

}
