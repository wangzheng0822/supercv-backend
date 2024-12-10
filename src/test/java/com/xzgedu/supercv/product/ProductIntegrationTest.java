package com.xzgedu.supercv.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // 清理或初始化数据
    }

    @Test
    void getProductInfo_Success() throws Exception {
        Product product = new Product();
        product.setName("产品1");
        product.setOriginalPrice(new BigDecimal("9.99"));
        product.setDiscountPrice(new BigDecimal("8.99"));
        product.setDurationDays(30);
        product.setAiAnalysisNum(10);
        product.setAiOptimizationNum(20);
        productService.addProduct(product);

        MvcResult result = mockMvc.perform(get("/v1/product/info")
                        .header("Origin", "https://www.supercv.cn")
                        .param("product_id", String.valueOf(product.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Product> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<Product>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        Product resProduct = responseData.getData();
        assertThat(resProduct).isNotNull();
        assertEquals(product.getName(), resProduct.getName());
        assertEquals(product.getOriginalPrice(), resProduct.getOriginalPrice());
        assertEquals(product.getDiscountPrice(), resProduct.getDiscountPrice());
        assertEquals(product.getDurationDays(), resProduct.getDurationDays());
        assertEquals(product.getAiAnalysisNum(), resProduct.getAiAnalysisNum());
        assertEquals(product.getAiOptimizationNum(), resProduct.getAiOptimizationNum());
    }
}
