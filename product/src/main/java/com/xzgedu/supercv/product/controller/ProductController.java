package com.xzgedu.supercv.product.controller;

import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "产品")
@RequestMapping("/v1/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "获取产品信息")
    @GetMapping("/info")
    public Product getProductById(@RequestParam("product_id") int productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "获取产品列表")
    @GetMapping("/list")
    public List<Product> listProducts() {
        return productService.getAllProducts();
    }
}
