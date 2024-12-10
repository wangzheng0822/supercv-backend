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

    public boolean addProduct(Product product) {
        return productRepo.addProduct(product);
    }

    public Product getProductById(long id) {
        return productRepo.getProductById(id);
    }

    public List<Product> listProducts(int limitOffset, int limitSize) {
        return productRepo.listProduct(limitOffset, limitSize);
    }


    public boolean deleteProduct(long id) {
        return productRepo.deleteProduct(id, true);
    }


    public boolean updateProduct(Product product) {
        return productRepo.updateProduct(product);
    }

    public int countProductsByName(String name) {
        return productRepo.countProductsByName(name);
    }

    public Product getProductByName(String name) {
        return productRepo.getProductByName(name);
    }
}
