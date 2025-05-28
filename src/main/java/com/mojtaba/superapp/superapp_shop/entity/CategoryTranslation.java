package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_translations", schema = "superapp")
public class CategoryTranslation {
    @EmbeddedId
    private CategoryTranslationId id;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank(message = "Language code cannot be blank")
    @Size(min = 2, max = 2, message = "Language code must be exactly 2 characters")
    @Column(length = 2, nullable = false)
    private String langCode;

    @NotBlank(message = "Name cannot be blank")
    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}

