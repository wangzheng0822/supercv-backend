package com.xzgedu.supercv.product.repo;

import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {

    @Autowired
    private ProductMapper productMapper;

    public Product getProductById(long id) {
        return productMapper.selectProductById(id);
    }

    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return productMapper.insertProduct(product) == 1;
    }

    public boolean updateProduct(Product product) {
        return productMapper.updateProduct(product) == 1;
    }

    public boolean deleteProduct(long id) {
        return productMapper.deleteProduct(id) == 1;
    }
}
