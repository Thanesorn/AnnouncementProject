package com.int221.backend.controller;


import com.int221.backend.dto.CategoryDto;
import com.int221.backend.dto.OutputAnnouceDto;
import com.int221.backend.dto.OutputCategoryDto;
import com.int221.backend.entities.Category;
import com.int221.backend.mapper.ListMapper;
import com.int221.backend.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Integer id){
        return modelMapper.map(categoryService.getCategoryById(id), CategoryDto.class);
    }




}
