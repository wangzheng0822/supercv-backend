package com.xzgedu.supercv.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.enums.PaymentChannelType;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.order.service.PaymentChannelService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
    }

    @Test
    public void createOrder_Success() throws Exception {
        //添加产品
        Product product = new Product();
        product.setName("Test Product");
        product.setOriginalPrice(BigDecimal.valueOf(10.0));
        product.setDiscountPrice(BigDecimal.valueOf(9.9));
        product.setDurationDays(30);
        product.setAiAnalysisNum(5);
        product.setAiOptimizationNum(3);
        productService.addProduct(product);

        //添加支付渠道
        int paymentChannelType = PaymentChannelType.WXPAY.getValue();
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setType(paymentChannelType);
        paymentChannel.setName("wx-1");
        paymentChannel.setAppId("wx-app-id-1");
        paymentChannel.setMchId("wx-mch-id-1");
        paymentChannel.setSecret("wx-secret-1");
        paymentChannel.setEnabled(true);
        paymentChannelService.addPaymentChannel(paymentChannel);

        MvcResult result = mockMvc.perform(post("/v1/order/create")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("product_id", String.valueOf(product.getId()))
                        .param("payment_channel_type", String.valueOf(paymentChannelType))
                        .param("final_price", "9.9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String,Object>> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Map<String,Object>>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        Order createdOrder = orderService.getOrderByNo((String) responseData.getData().get("orderNo"));
        System.out.println(createdOrder.toString());
        assertEquals(product.getId(), createdOrder.getProductId());
        assertEquals(paymentChannelType, createdOrder.getPaymentChannelType());
    }

    @Test
    public void getOrderByNo_Success() throws Exception {
        Order order = new Order();
        order.setUid(authToken.getUid());
        order.setOrderTime(new Date());
        order.setOrderNo("1456678");
        order.setProductId(1L);
        order.setProductName("Test Product");
        order.setPaymentChannelType(1);
        order.setPaymentChannelId(1L);
        order.setPaymentAmount(BigDecimal.valueOf(9.9));
        orderService.addOrder(order);

        MvcResult result = mockMvc.perform(get("/v1/order/detail")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                         .param("no", "1456678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Order> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Order>>() {});

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        Order retrievedOrder = responseData.getData();
        assertEquals(order.getOrderNo(), retrievedOrder.getOrderNo());
    }

    @Test
    public void closePayment_Success() throws Exception {
        Order order = new Order();
        order.setUid(authToken.getUid());
        order.setOrderTime(new Date());
        order.setOrderNo("1456678");
        order.setProductId(1L);
        order.setProductName("Test Product");
        order.setPaymentChannelType(1);
        order.setPaymentChannelId(1L);
        order.setPaymentAmount(BigDecimal.valueOf(9.9));
        order.setPaymentStatus(1);
        orderService.addOrder(order);

        MvcResult result = mockMvc.perform(post("/v1/order/close")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("no", "1456678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Void> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Void>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());

        Order closedOrder = orderService.getOrderById(order.getId());
        assertEquals(PaymentStatus.CLOSED.getValue(), closedOrder.getPaymentStatus());
    }

    @Test
    public void getMyOrders_Success() throws Exception {
        Order order = new Order();
        order.setUid(authToken.getUid());
        order.setOrderTime(new Date());
        order.setOrderNo("1456678");
        order.setProductId(1L);
        order.setProductName("Test Product");
        order.setPaymentChannelType(1);
        order.setPaymentChannelId(1L);
        order.setPaymentAmount(BigDecimal.valueOf(9.9));
        order.setPaymentStatus(1);
        orderService.addOrder(order);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("page_no", "1")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = objectMapper.readValue(
                content, new TypeReference<ResponseData<Map<String, Object>>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertEquals(1, (Integer) responseData.getData().get("count"));
    }
}
