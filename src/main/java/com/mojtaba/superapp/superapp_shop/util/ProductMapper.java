package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CreateProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductTranslationDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateProductDto;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ProductImage;
import com.mojtaba.superapp.superapp_shop.entity.ProductTranslation;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final CategoryRepository catRepo;

    @Autowired
    public ProductMapper(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }

    public Product fromCreateDto(CreateProductDto dto) {
        Product p = new Product();
        p.setSku(dto.getSku());
        p.setPrice(dto.getPrice());
        p.setCategory(catRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId())));
        if (dto.getTranslations() != null) {
            p.setTranslations(dto.getTranslations().stream()
                    .map(t -> {
                        ProductTranslation pt = new ProductTranslation();
                        pt.setLangCode(t.getLangCode());
                        pt.setName(t.getName());
                        pt.setDescription(t.getDescription());
                        pt.setProduct(p);
                        return pt;
                    })
                    .toList());
        }
        if (dto.getImageUrls() != null) {
            p.setImages(dto.getImageUrls().stream()
                    .map(url -> {
                        ProductImage pi = new ProductImage();
                        pi.setUrl(url);
                        pi.setProduct(p);
                        return pi;
                    })
                    .toList());
        }
        return p;
    }

    public Product fromUpdateDto(UpdateProductDto dto) {
        Product p = new Product();
        p.setPrice(dto.getPrice());
        p.setCategory(catRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId())));
        if (dto.getTranslations() != null) {
            p.setTranslations(dto.getTranslations().stream()
                    .map(t -> {
                        ProductTranslation pt = new ProductTranslation();
                        pt.setLangCode(t.getLangCode());
                        pt.setName(t.getName());
                        pt.setDescription(t.getDescription());
                        pt.setProduct(p);
                        return pt;
                    })
                    .toList());
        }
        if (dto.getImageUrls() != null) {
            p.setImages(dto.getImageUrls().stream()
                    .map(url -> {
                        ProductImage pi = new ProductImage();
                        pi.setUrl(url);
                        pi.setProduct(p);
                        return pi;
                    })
                    .toList());
        }
        return p;
    }

    public ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setProductId(p.getProductId());
        dto.setSku(p.getSku());
        dto.setPrice(p.getPrice());
        dto.setCategoryId(p.getCategory().getCategoryId());
        dto.setTranslations(p.getTranslations().stream()
                .map(t -> new ProductTranslationDto(t.getLangCode(), t.getName(), t.getDescription()))
                .toList());
        dto.setImageUrls(p.getImages().stream().map(ProductImage::getUrl).toList());
        return dto;
    }
}
