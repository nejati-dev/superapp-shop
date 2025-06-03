package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.CreateProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductDto;
import com.mojtaba.superapp.superapp_shop.dto.ProductTranslationDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateProductDto;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.ProductService;
import com.mojtaba.superapp.superapp_shop.util.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductDto sampleDto;

    /**
     * ControllerAdvice ساده برای تبدیل ResourceNotFoundException به HTTP 404
     */
    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();

        sampleDto = new ProductDto();
        sampleDto.setProductId(10);
        sampleDto.setCategoryId(5);
        sampleDto.setSku("SKU-10");
        sampleDto.setPrice(new BigDecimal("9.99"));
        sampleDto.setTranslations(List.of(new ProductTranslationDto("en", "NameEN", "DescEN")));
        sampleDto.setImageUrls(List.of("img1.png"));
    }

    @Test
    void createProduct_validInput_shouldReturn201AndDto() throws Exception {
        CreateProductDto createDto = new CreateProductDto();
        createDto.setCategoryId(5);
        createDto.setSku("SKU-10");
        createDto.setPrice(new BigDecimal("9.99"));
        createDto.setTranslations(List.of(new ProductTranslationDto("en", "NameEN", "DescEN")));
        createDto.setImageUrls(List.of("img1.png"));

        when(productMapper.fromCreateDto(any(CreateProductDto.class))).thenReturn(new Product());
        when(productService.createProduct(any(Product.class))).thenReturn(new Product());
        when(productMapper.toDto(any(Product.class))).thenReturn(sampleDto);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.sku").value("SKU-10"))
                .andExpect(jsonPath("$.price").value(9.99));
    }

    @Test
    void getProductById_existing_shouldReturnDto() throws Exception {
        when(productService.getProductById(10)).thenReturn(Optional.of(new Product()));
        when(productMapper.toDto(any(Product.class))).thenReturn(sampleDto);

        mockMvc.perform(get("/api/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.sku").value("SKU-10"));
    }

    @Test
    void getProductById_notFound_shouldReturn404() throws Exception {
        when(productService.getProductById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_shouldReturnList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(new Product()));
        when(productMapper.toDto(any(Product.class))).thenReturn(sampleDto);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(10));
    }

    @Test
    void updateProduct_existing_shouldReturnUpdatedDto() throws Exception {
        UpdateProductDto updateDto = new UpdateProductDto();
        updateDto.setCategoryId(5);
        updateDto.setPrice(new BigDecimal("9.99"));
        updateDto.setTranslations(List.of(new ProductTranslationDto("en", "NameEN", "DescEN")));
        updateDto.setImageUrls(List.of("img1.png"));

        Product updatedEntity = new Product();

        when(productMapper.fromUpdateDto(any(UpdateProductDto.class))).thenReturn(updatedEntity);
        when(productService.updateProduct(eq(10), any(Product.class))).thenReturn(updatedEntity);
        when(productMapper.toDto(updatedEntity)).thenReturn(sampleDto);

        mockMvc.perform(put("/api/products/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-10"));
    }

    @Test
    void updateProduct_notFound_shouldReturn404() throws Exception {
        // ساخت یک JSON معتبر از UpdateProductDto
        UpdateProductDto updateDto = new UpdateProductDto();
        updateDto.setCategoryId(5);
        updateDto.setPrice(new BigDecimal("9.99"));
        updateDto.setTranslations(List.of(new ProductTranslationDto("en", "NameEN", "DescEN")));
        updateDto.setImageUrls(List.of("img1.png"));

        // این stub حتماً باید دقیقاً برای (99, null) تعریف شود
        when(productService.updateProduct(eq(99), isNull()))
                .thenThrow(new ResourceNotFoundException("Product", "id", 99));

        mockMvc.perform(put("/api/products/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_shouldReturn204() throws Exception {
        doNothing().when(productService).deleteProduct(10);

        mockMvc.perform(delete("/api/products/10"))
                .andExpect(status().isNoContent());
    }
}
