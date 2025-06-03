// src/test/java/com/mojtaba/superapp/superapp_shop/util/ProductMapperTest.java
package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CreateProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductTranslationDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateProductDto;
import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ProductImage;
import com.mojtaba.superapp.superapp_shop.entity.ProductTranslation;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProductMapperTest {

    private CategoryRepository catRepo;
    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        catRepo = Mockito.mock(CategoryRepository.class);
        mapper = new ProductMapper(catRepo);
    }

    @Test
    void fromCreateDto_validInput_mapsAllFields() {
        // Prepare DTO
        CreateProductDto dto = new CreateProductDto();
        dto.setCategoryId(5);
        dto.setSku("SKU-1");
        dto.setPrice(new BigDecimal("99.99"));
        dto.setTranslations(List.of(
                new ProductTranslationDto("en", "NameEN", "DescEN"),
                new ProductTranslationDto("fa", "NameFA", "DescFA")
        ));
        dto.setImageUrls(List.of("url1.jpg", "url2.jpg"));

        // Mock category lookup
        Category cat = new Category();
        cat.setCategoryId(5);
        when(catRepo.findById(5)).thenReturn(Optional.of(cat));

        // Execute
        Product p = mapper.fromCreateDto(dto);

        // Assertions
        assertThat(p).isNotNull();
        assertThat(p.getSku()).isEqualTo("SKU-1");
        assertThat(p.getPrice()).isEqualByComparingTo("99.99");
        assertThat(p.getCategory()).isSameAs(cat);

        // Translations
        assertThat(p.getTranslations()).hasSize(2)
                .extracting(ProductTranslation::getLangCode, ProductTranslation::getName, ProductTranslation::getDescription)
                .containsExactlyInAnyOrder(
                        tuple("en", "NameEN", "DescEN"),
                        tuple("fa", "NameFA", "DescFA")
                );
        // Each translation's product reference
        p.getTranslations().forEach(pt -> assertThat(pt.getProduct()).isSameAs(p));

        // Images
        assertThat(p.getImages()).hasSize(2)
                .extracting(ProductImage::getUrl)
                .containsExactlyInAnyOrder("url1.jpg", "url2.jpg");
        p.getImages().forEach(pi -> assertThat(pi.getProduct()).isSameAs(p));

        verify(catRepo).findById(5);
    }

    @Test
    void fromCreateDto_nonExistingCategory_throwsException() {
        CreateProductDto dto = new CreateProductDto();
        dto.setCategoryId(10);
        dto.setSku("ABC");
        dto.setPrice(new BigDecimal("10.00"));
        when(catRepo.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mapper.fromCreateDto(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void fromUpdateDto_validInput_mapsFields() {
        UpdateProductDto dto = new UpdateProductDto();
        dto.setCategoryId(7);
        dto.setPrice(new BigDecimal("55.55"));
        dto.setTranslations(List.of(new ProductTranslationDto("en", "NewName", "NewDesc")));
        dto.setImageUrls(List.of("new1.png"));

        Category cat = new Category();
        cat.setCategoryId(7);
        when(catRepo.findById(7)).thenReturn(Optional.of(cat));

        Product p = mapper.fromUpdateDto(dto);

        assertThat(p).isNotNull();
        assertThat(p.getPrice()).isEqualByComparingTo("55.55");
        assertThat(p.getCategory()).isSameAs(cat);

        assertThat(p.getTranslations()).hasSize(1)
                .extracting(ProductTranslation::getLangCode, ProductTranslation::getName, ProductTranslation::getDescription)
                .containsExactly(tuple("en", "NewName", "NewDesc"));
        assertThat(p.getImages()).hasSize(1)
                .extracting(ProductImage::getUrl)
                .containsExactly("new1.png");

        verify(catRepo).findById(7);
    }

    @Test
    void toDto_mapsEntityToDtoCorrectly() {
        // Prepare entity
        Category cat = new Category();
        cat.setCategoryId(3);

        Product p = new Product();
        p.setProductId(99);
        p.setSku("SKU-XYZ");
        p.setPrice(new BigDecimal("123.45"));
        p.setCategory(cat);

        // Add translations
        ProductTranslation t1 = new ProductTranslation();
        t1.setLangCode("en");
        t1.setName("Widget");
        t1.setDescription("A widget");
        t1.setProduct(p);

        ProductTranslation t2 = new ProductTranslation();
        t2.setLangCode("fr");
        t2.setName("Gadget");
        t2.setDescription("Un gadget");
        t2.setProduct(p);

        p.setTranslations(List.of(t1, t2));

        // Add images
        ProductImage img1 = new ProductImage();
        img1.setUrl("img1.jpg");
        img1.setProduct(p);
        ProductImage img2 = new ProductImage();
        img2.setUrl("img2.jpg");
        img2.setProduct(p);

        p.setImages(List.of(img1, img2));

        // Execute
        ProductDto dto = mapper.toDto(p);

        // Assertions
        assertThat(dto.getProductId()).isEqualTo(99);
        assertThat(dto.getSku()).isEqualTo("SKU-XYZ");
        assertThat(dto.getPrice()).isEqualByComparingTo("123.45");
        assertThat(dto.getCategoryId()).isEqualTo(3);

        assertThat(dto.getTranslations()).hasSize(2)
                .extracting(ProductTranslationDto::getLangCode, ProductTranslationDto::getName, ProductTranslationDto::getDescription)
                .containsExactlyInAnyOrder(
                        tuple("en", "Widget", "A widget"),
                        tuple("fr", "Gadget", "Un gadget")
                );

        assertThat(dto.getImageUrls()).containsExactlyInAnyOrder("img1.jpg", "img2.jpg");
    }
}

