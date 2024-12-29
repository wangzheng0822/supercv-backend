package com.xzgedu.supercv.product.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private long id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private int durationDays;
    private int aiAnalysisNum;
    private int aiOptimizationNum;
    private int sortValue;
}
