package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.util.ProductMapper;
import com.mojtaba.superapp.superapp_shop.dto.CreateProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateProductDto;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;
    private final ProductMapper mapper;

    public ProductController(ProductService svc, ProductMapper mapper) {
        this.svc = svc;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody CreateProductDto dto) {
        Product p = svc.createProduct(mapper.fromCreateDto(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(p));
    }

    @GetMapping
    public List<ProductDto> all() {
        return svc.getAllProducts().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProductDto one(@PathVariable Integer id) {
        return svc.getProductById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Integer id,
                             @Valid @RequestBody UpdateProductDto dto) {
        Product p = svc.updateProduct(id, mapper.fromUpdateDto(dto));
        return mapper.toDto(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        svc.deleteProduct(id);
    }
}
