package com.xzgedu.supercv.product.domain;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

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
    @JsonIgnore
    private boolean isDeleted;
}
