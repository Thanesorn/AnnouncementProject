package com.int221.backend.services;

import com.int221.backend.entities.Announce;
import com.int221.backend.entities.Category;
import com.int221.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


  public Category getCategoryById(Integer id){
      return categoryRepository.findById(id).orElseThrow(()->
              new ResponseStatusException(HttpStatus.NOT_FOUND, "Category id" + " does not exist"));
  }




}
