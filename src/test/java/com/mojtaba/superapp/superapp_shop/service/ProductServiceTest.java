// src/test/java/com/mojtaba/superapp/superapp_shop/service/ProductServiceImplTest.java
package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    private Product p1, p2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category cat = new Category();
        cat.setCategoryId(1);

        p1 = new Product();
        p1.setProductId(10);
        p1.setCategory(cat);
        p1.setSku("SKU10");
        p1.setPrice(new BigDecimal("10.00"));

        p2 = new Product();
        p2.setProductId(20);
        p2.setCategory(cat);
        p2.setSku("SKU20");
        p2.setPrice(new BigDecimal("20.00"));
    }

    @Test
    void createProduct_shouldCallSaveAndReturnEntity() {
        when(productRepository.save(p1)).thenReturn(p1);

        Product result = service.createProduct(p1);
        verify(productRepository).save(p1);
        assertThat(result).isEqualTo(p1);
    }

    @Test
    void getProductById_existing_returnsOptional() {
        when(productRepository.findById(10)).thenReturn(Optional.of(p1));

        Optional<Product> found = service.getProductById(10);
        verify(productRepository).findById(10);
        assertThat(found).isPresent().contains(p1);
    }

    @Test
    void getProductById_nonExisting_returnsEmpty() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Product> found = service.getProductById(999);
        verify(productRepository).findById(999);
        assertThat(found).isEmpty();
    }

    @Test
    void getAllProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Product> all = service.getAllProducts();
        verify(productRepository).findAll();
        assertThat(all).hasSize(2).containsExactlyInAnyOrder(p1, p2);
    }

    @Test
    void updateProduct_existing_updatesFieldsAndSaves() {
        Product updates = new Product();
        updates.setCategory(p2.getCategory());
        updates.setSku("NEW-SKU");
        updates.setPrice(new BigDecimal("30.00"));

        when(productRepository.findById(10)).thenReturn(Optional.of(p1));
        when(productRepository.save(any(Product.class))).thenReturn(p1);

        Product result = service.updateProduct(10, updates);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product toSave = captor.getValue();

        assertThat(toSave.getSku()).isEqualTo("NEW-SKU");
        assertThat(toSave.getPrice()).isEqualByComparingTo("30.00");
        assertThat(toSave.getCategory()).isEqualTo(p2.getCategory());

        assertThat(result).isEqualTo(p1);
    }

    @Test
    void updateProduct_nonExisting_throwsException() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateProduct(999, new Product()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void deleteProduct_shouldCallDeleteById() {
        doNothing().when(productRepository).deleteById(10);
        service.deleteProduct(10);
        verify(productRepository).deleteById(10);
    }
}

