package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product createProduct(Product p) {
        return repo.save(p);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product updateProduct(Integer id, Product p) {
        Product ex = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        ex.setPrice(p.getPrice());
        ex.setSku(p.getSku());
        ex.setCategory(p.getCategory());
        if (p.getTranslations() != null) {
            ex.getTranslations().clear();
            ex.getTranslations().addAll(p.getTranslations());
        }
        if (p.getImages() != null) {
            ex.getImages().clear();
            ex.getImages().addAll(p.getImages());
        }
        return repo.save(ex);
    }

    @Override
    public void deleteProduct(Integer id) {
        repo.deleteById(id);
    }
}
