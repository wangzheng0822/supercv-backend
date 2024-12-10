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

    public boolean addProduct(Product product) {
        return productMapper.insertProduct(product) == 1;
    }

    public Product getProductById(long id) {
        return productMapper.selectProductId(id);
    }

    public List<Product> listProduct(int limitOffset, int limitSize) {
        return productMapper.listProduct(limitOffset, limitSize);
    }

    public boolean deleteProduct(long id, boolean isDeleted) {
        return productMapper.deleteProduct(id, isDeleted) == 1;
    }

    public boolean updateProduct(Product product) {
        return productMapper.updateProduct(product) == 1;
    }

    public int countProductsByName(String name) {
        return productMapper.countProductsByName(name);
    }

    public Product getProductByName(String name) {
        return productMapper.selectProductByName(name);
    }
}
