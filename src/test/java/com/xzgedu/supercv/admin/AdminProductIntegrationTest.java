package com.xzgedu.supercv.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.AdminAccountSetUp;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.user.domain.AuthToken;
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
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class AdminProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminAccountSetUp adminAccountSetup;

    private AuthToken authToken;

    @BeforeEach
    public void setup() {
        this.authToken = adminAccountSetup.createAdminAccountAndLogin();
    }

    @Test
    public void addProduct_Success_ReturnsProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setOriginalPrice(new BigDecimal("100.00"));
        product.setDiscountPrice(new BigDecimal("90.00"));
        product.setDurationDays(30);
        product.setAiAnalysisNum(10);
        product.setAiOptimizationNum(5);
        product.setSortValue(1);

        MvcResult result = mockMvc.perform(post("/admin/product/add")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("name", product.getName())
                        .param("original_price", product.getOriginalPrice().toString())
                        .param("discount_price", product.getDiscountPrice().toString())
                        .param("duration_days", String.valueOf(product.getDurationDays()))
                        .param("ai_analysis_num", String.valueOf(product.getAiAnalysisNum()))
                        .param("ai_optimization_num", String.valueOf(product.getAiOptimizationNum()))
                        .param("sort_value", String.valueOf(product.getSortValue()))
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

    @Test
    public void listProducts_Success_ReturnsProductList() throws Exception {
        // Add a product first
        Product product = new Product();
        product.setName("Test Product");
        product.setOriginalPrice(new BigDecimal("100.00"));
        product.setDiscountPrice(new BigDecimal("90.00"));
        product.setDurationDays(30);
        product.setAiAnalysisNum(10);
        product.setAiOptimizationNum(5);
        product.setSortValue(1);

        MvcResult addResult = mockMvc.perform(post("/admin/product/add")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("name", product.getName())
                        .param("original_price", product.getOriginalPrice().toString())
                        .param("discount_price", product.getDiscountPrice().toString())
                        .param("duration_days", String.valueOf(product.getDurationDays()))
                        .param("ai_analysis_num", String.valueOf(product.getAiAnalysisNum()))
                        .param("ai_optimization_num", String.valueOf(product.getAiOptimizationNum()))
                        .param("sort_value", String.valueOf(product.getSortValue()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Get the product list
        MvcResult result = mockMvc.perform(get("/v1/product/list")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<List<Product>> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<List<Product>>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Product> productList = responseData.getData();
        assertThat(productList).isNotEmpty();
    }

    @Test
    public void updateProduct_Success_ReturnsNoContent() throws Exception {
        Product product = new Product();
        product.setName("Updated Product");
        product.setOriginalPrice(new BigDecimal("150.00"));
        product.setDiscountPrice(new BigDecimal("130.00"));
        product.setDurationDays(60);
        product.setAiAnalysisNum(20);
        product.setAiOptimizationNum(10);
        product.setSortValue(1);

        // Add a product first
        MvcResult addResult = mockMvc.perform(post("/admin/product/add")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("name", product.getName())
                        .param("original_price", product.getOriginalPrice().toString())
                        .param("discount_price", product.getDiscountPrice().toString())
                        .param("duration_days", String.valueOf(product.getDurationDays()))
                        .param("ai_analysis_num", String.valueOf(product.getAiAnalysisNum()))
                        .param("ai_optimization_num", String.valueOf(product.getAiOptimizationNum()))
                        .param("sort_value", String.valueOf(product.getSortValue()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String addContent = addResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Product> addResponseData = new ObjectMapper().readValue(
                addContent, new TypeReference<ResponseData<Product>>() {});
        Product addedProduct = addResponseData.getData();

        // Update the product
        MvcResult updateResult = mockMvc.perform(post("/admin/product/update")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("product_id", String.valueOf(addedProduct.getId()))
                        .param("name", "Updated Product Name")
                        .param("original_price", new BigDecimal("200.00").toString())
                        .param("discount_price", new BigDecimal("180.00").toString())
                        .param("duration_days", String.valueOf(90))
                        .param("ai_analysis_num", String.valueOf(30))
                        .param("ai_optimization_num", String.valueOf(15))
                        .param("sort_value", String.valueOf(2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the update
        MvcResult getResult = mockMvc.perform(get("/v1/product/list")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getContent = getResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<List<Product>> getResponseData = new ObjectMapper().readValue(
                getContent, new TypeReference<ResponseData<List<Product>>>() {});
        List<Product> productList = getResponseData.getData();
        assertThat(productList).isNotEmpty();
        Product updatedProduct = productList.stream()
                .filter(p -> Objects.equals(p.getId(), addedProduct.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product Name", updatedProduct.getName());
        assertEquals(new BigDecimal("200.00"), updatedProduct.getOriginalPrice());
        assertEquals(new BigDecimal("180.00"), updatedProduct.getDiscountPrice());
        assertEquals(90, updatedProduct.getDurationDays());
        assertEquals(30, updatedProduct.getAiAnalysisNum());
        assertEquals(15, updatedProduct.getAiOptimizationNum());
    }

    @Test
    public void deleteProduct_Success_ReturnsNoContent() throws Exception {
        // Add a product first
        Product product = new Product();
        product.setName("Product to Delete");
        product.setOriginalPrice(new BigDecimal("100.00"));
        product.setDiscountPrice(new BigDecimal("90.00"));
        product.setDurationDays(30);
        product.setAiAnalysisNum(10);
        product.setAiOptimizationNum(5);
        product.setSortValue(1);

        MvcResult addResult = mockMvc.perform(post("/admin/product/add")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("name", product.getName())
                        .param("original_price", product.getOriginalPrice().toString())
                        .param("discount_price", product.getDiscountPrice().toString())
                        .param("duration_days", String.valueOf(product.getDurationDays()))
                        .param("ai_analysis_num", String.valueOf(product.getAiAnalysisNum()))
                        .param("ai_optimization_num", String.valueOf(product.getAiOptimizationNum()))
                        .param("sort_value", String.valueOf(product.getSortValue()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String addContent = addResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Product> addResponseData = new ObjectMapper().readValue(
                addContent, new TypeReference<ResponseData<Product>>() {});
        Product addedProduct = addResponseData.getData();

        // Delete the product
        MvcResult deleteResult = mockMvc.perform(post("/admin/product/delete")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .param("product_id", String.valueOf(addedProduct.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the deletion
        MvcResult getResult = mockMvc.perform(get("/v1/product/list")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", authToken.getUid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getContent = getResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<List<Product>> getResponseData = new ObjectMapper().readValue(
                getContent, new TypeReference<ResponseData<List<Product>>>() {});
        List<Product> productList = getResponseData.getData();
        assertThat(productList).doesNotContain(addedProduct);
    }
}
