package com.int221.backend.repositories;

import com.int221.backend.dto.CategoryDto;
import com.int221.backend.entities.Announce;
import com.int221.backend.entities.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId")
    List<Category> findCategoriesByCategoryId(Integer categoryId);
}