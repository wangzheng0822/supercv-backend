package com.xzgedu.supercv.product.controller;

import com.xzgedu.supercv.common.exception.DataInvalidException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.exception.ProductNotFoundException;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "产品接口")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "添加新产品")
    @PostMapping(path = "/create")
    public void addProduct(@RequestBody Product product) throws GenericBizException {
        // 参数校验
        validProductParam(product);

        if (productService.countProductsByName(product.getName()) > 0) {
            throw new DataInvalidException(ErrorCode.GENERIC_DATA_INVALID,
                    "There is already a product with the same name in the database");
        }


        if (!productService.addProduct(product)) {
            throw new GenericBizException("Failed to add product: " + product);
        }

    }


    @Operation(summary = "修改产品信息")
    @PostMapping(path = "/update")
    public void updateProduct(@RequestBody Product product) throws GenericBizException {

        // 验证产品参数
        validProductParam(product);

        // 验证是否已经有相同名称的产品
        Product oldProduct = productService.getProductById(product.getId());
        if (!oldProduct.getName().equals(product.getName())) {
            if (productService.countProductsByName(product.getName()) > 0) {
                throw new DataInvalidException(ErrorCode.GENERIC_DATA_INVALID,
                        "There is already a product with the same name in the database");
            }
        }

        if (!productService.updateProduct(product)) {
            throw new GenericBizException("Failed to add product: " + product);
        }

    }

    @Operation(summary = "删除产品")
    @GetMapping(path = "/delete")
    public void deleteProduct(@RequestParam("id") long id) throws ProductNotFoundException, GenericBizException {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("Failed to find product: " + id);
        }
        if (!productService.deleteProduct(product.getId())) {
            throw new GenericBizException("Failed to delete product: " + id);
        }
    }

    @Operation(summary = "分页查询产品")
    @GetMapping(path = "/list")
    public List<Product> listProduct(
            @RequestParam(value = "page_no") Integer pageNo,
            @RequestParam(value = "page_size") Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        return new ArrayList<>(productService.listProducts(limitOffset, limitSize));
    }

    @Operation(summary = "按照id查询产品")
    @GetMapping(path = "/query")
    public Product queryProduct(@RequestParam("id") long id) throws ProductNotFoundException {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: [id=" + id + "]");
        }
        return product;
    }


    private void validProductParam(Product product) {
        // 校验对象字段是否为空
        Assert.checkNotNull(product.getName());
        Assert.checkNotNull(product.getOriginalPrice());
        Assert.checkNotNull(product.getDiscountPrice());

        // 检查值是否大于0
        int durationDays = product.getDurationDays();
        Assert.checkPositiveNumber(durationDays, "check duration_days is a positive number failed: [duration_days=" +  durationDays + "]");
        int aiAnalysisNum = product.getAiAnalysisNum();
        Assert.checkPositiveNumber(aiAnalysisNum, "check ai_analysis_num is a positive number failed: [ai_analysis_num=" + aiAnalysisNum + "]");
        int aiOptimizationNum = product.getAiOptimizationNum();
        Assert.checkPositiveNumber(aiOptimizationNum, "check ai_optimization_num is a positive number failed: [ai_optimization_num=" + aiAnalysisNum + "]");
        BigDecimal originalPrice = product.getOriginalPrice();
        Assert.checkPositiveNumber(originalPrice.signum(), "check original_price is a positive number failed: [original_price=" + originalPrice + "]");
        BigDecimal discountPrice = product.getDiscountPrice();
        Assert.checkPositiveNumber(discountPrice.signum(), "check discount_price is a positive number failed: [discount_price=" + discountPrice + "]");
    }

}


