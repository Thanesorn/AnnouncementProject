package com.int221.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    @Column(name = "categoryName", nullable = false, length = 50)
    private String categoryName;

    @JsonIgnore
    @OneToMany(mappedBy = "announcementCategory")
    private Set<Announce> announce = new LinkedHashSet<>();
}