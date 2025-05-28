package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product p);
    Optional<Product> getProductById(Integer id);
    List<Product> getAllProducts();
    Product updateProduct(Integer id, Product p);
    void deleteProduct(Integer id);
}
