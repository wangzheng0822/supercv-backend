package com.xzgedu.supercv.product;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    private Product randomProduct;


    @BeforeEach
    public void setup() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
        this.randomProduct = createRandomProductAndStore();
    }

    public Product createRandomProductAndStore() {
        Product product = createProduct(null);
        productService.addProduct(product);
        return productService.getProductByName(product.getName());
    }

    public Product createProduct(Long id) {
        Product product = new Product();
        if(id != null){
            product.setId(id);
        }
        product.setName("product" + StringRandom.genStrByDigitAndLetter(4));
        product.setAiAnalysisNum(Integer.parseInt(StringRandom.genStrByDigit(2)));
        product.setAiOptimizationNum(Integer.parseInt(StringRandom.genStrByDigit(2)));
        BigDecimal originalPrice = new BigDecimal(StringRandom.genStrByDigit(4) + "." + StringRandom.genStrByDigit(2));
        product.setOriginalPrice(originalPrice);
        BigDecimal discountPrice = new BigDecimal(StringRandom.genStrByDigit(4) + "." + StringRandom.genStrByDigit(2));
        product.setDiscountPrice(discountPrice);
        return product;
    }

    @Test
    void getProductInfo_ProductNotFound_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        long productiId = 100;

        MvcResult result = mockMvc.perform(get("/v1/product/query")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("id", String.valueOf(productiId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });

        assertEquals(ErrorCode.PRODUCT_NOT_EXISTED.getCode(), responseData.getCode());
    }

    @Test
    void getProductInfo_ProductFound_ReturnsProduct() throws Exception {
        long uid = authToken.getUid();
        long id = randomProduct.getId();

        MvcResult result = mockMvc.perform(get("/v1/product/query")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("id", String.valueOf(id))
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {
        });

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertEquals(id, responseData.getData().getId());
    }


    @Test
    void createProduct_Successful() throws Exception {
        long uid = authToken.getUid();
        Product product = createProduct(null);

        MvcResult result = mockMvc.perform(post("/v1/product/create")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
    }

    @Test
    void createProduct_SameName_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        Product product = createProduct(null);
        String name = randomProduct.getName();
        product.setName(name);

        MvcResult result = mockMvc.perform(post("/v1/product/create")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.GENERIC_DATA_INVALID.getCode(), responseData.getCode());
    }


    @Test
    void createProduct_ParamIsNULL_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        Product product = createProduct(null);
        product.setDiscountPrice(null);

        MvcResult result = mockMvc.perform(post("/v1/product/create")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.DATA_SHOULD_NOT_EMPTY.getCode(), responseData.getCode());

    }

    @Test
    void createProduct_ParamIsNotPositiveNum_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        Product product = createProduct(null);
        product.setDurationDays(-1);

        MvcResult result = mockMvc.perform(post("/v1/product/create")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.GENERIC_DATA_INVALID.getCode(), responseData.getCode());
    }


    @Test
    void updateProduct_Successful() throws Exception {
        long uid = authToken.getUid();
        randomProduct.setDurationDays(20);

        MvcResult result = mockMvc.perform(post("/v1/product/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(randomProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
    }

    @Test
    void updateProduct_SameNameProduct_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        Product sameProduct = createRandomProductAndStore();
        sameProduct.setName(randomProduct.getName());

        MvcResult result = mockMvc.perform(post("/v1/product/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(sameProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.GENERIC_DATA_INVALID.getCode(), responseData.getCode());
    }

    @Test
    void updateProduct_ParamIsNull_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        randomProduct.setName(null);

        MvcResult result = mockMvc.perform(post("/v1/product/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(randomProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.DATA_SHOULD_NOT_EMPTY.getCode(), responseData.getCode());
    }

    @Test
    void updateProduct_ParamIsNotPositiveNum_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        randomProduct.setDiscountPrice(new BigDecimal("-12.0"));

        MvcResult result = mockMvc.perform(post("/v1/product/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(randomProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.GENERIC_DATA_INVALID.getCode(), responseData.getCode());
    }

    @Test
    void deleteProduct_Successful() throws Exception {
        long uid = authToken.getUid();
        long id = randomProduct.getId();

        MvcResult result = mockMvc.perform(get("/v1/product/delete")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("id", String.valueOf(id))
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Product> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Product>>() {});

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
    }

    @Test
    void deleteProduct_ProductNotFound_ReturnsError() throws Exception {
        long uid = authToken.getUid();
        long id = randomProduct.getId() + 1;

        MvcResult result = mockMvc.perform(get("/v1/product/delete")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("id", String.valueOf(id))
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });

        assertEquals(ErrorCode.PRODUCT_NOT_EXISTED.getCode(), responseData.getCode());
    }
}
