package com.xzgedu.supercv.product.service;

import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product getProductById(long id) {
        return productRepo.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return productRepo.addProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productRepo.updateProduct(product);
    }

    public boolean deleteProduct(long id) {
        return productRepo.deleteProduct(id);
    }
}
