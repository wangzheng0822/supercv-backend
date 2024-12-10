package com.xzgedu.supercv.order;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.user.domain.AuthToken;
import org.assertj.core.util.DateUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    private Order randomOrder;


    // 支付状态
    Integer PAY_PENDING = 0;
    Integer PAY_SUCCESS = 1;
    Integer PAY_OVERTIME = 2;
    Integer PAY_FAILED = 3;

    // 权益状态
    Integer GRANT_PENDING = 0;
    Integer GRANT_SUCCESS = 1;
    Integer GRANT_FAILED = 2;

    // 支付渠道
    Integer PAY_CHANNEL_TYPE_WECHAT = 0;
    Integer PAY_CHANNEL_TYPE_ALIPAY = 1;

    String PAYMENTNO3RD = "123456";

    @BeforeEach
    public void setup() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
        this.randomOrder = createRandomOrder();
    }

    public Order createRandomOrder() {
        Order order = new Order();
        order.setUserId(authToken.getUid());
        order.setOrderNo(orderService.generateOrderNo(authToken.getUid()));
        order.setProductId(1l);
        order.setOrderTime(DateUtil.now());
        order.setPaymentStatus( 0);
        order.setGrantStatus(0);
        orderService.addOrder(order);
        return order;
    }

    @Test
    void getOrderInfo_OrderFound_ReturnsOrder() throws Exception {
        Order order = randomOrder;

        MvcResult result = mockMvc.perform(get("/v1/order/query")
                        .param("orderNo", String.valueOf(order.getOrderNo()))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Order> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Order>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(),responseData.getCode());
        assertNotNull(responseData.getData());
        assertEquals(order.getOrderNo(),responseData.getData().getOrderNo());
    }

    @Test
    void getOrderListByUserTest_success() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/order/list_by_uid")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .param("userId", String.valueOf(authToken.getUid()))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(),responseData.getCode());
        assertNotNull(responseData.getData());

        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(authToken.getUid(), orders.get(0).getUserId());
    }

    @Test
    void getOrderListByUserIdTest_success() throws Exception {
        Order testOrder = new Order();
        testOrder.setUserId(authToken.getUid());
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(),responseData.getCode());
        assertNotNull(responseData.getData());

        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getUserId(), orders.get(0).getUserId());
    }

    @Test
    void getOrderListByOrderNoTest_success() throws Exception {
        Order testOrder = new Order();
        testOrder.setOrderNo(randomOrder.getOrderNo());
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(),responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getOrderNo(),orders.get(0).getOrderNo());
    }

    @Test
    void getOrderListByProductIdTest_success() throws Exception {
        Order testOrder = new Order();
        testOrder.setProductId(randomOrder.getProductId());
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getProductId(), orders.get(0).getProductId());
    }

    @Test
    void getOrderListByPaymentChannelTypeTest_success() throws Exception {
        Integer paymentChannelType = PAY_CHANNEL_TYPE_WECHAT;
        String paymentNo3rd = PAYMENTNO3RD;
        Long paymentChannelId = 1l;

        orderService.updatePaymentStatus(randomOrder.getId(), PAY_SUCCESS, paymentNo3rd, paymentChannelType, paymentChannelId);

        Order testOrder = new Order();
        testOrder.setId(randomOrder.getId());
        testOrder.setPaymentChannelType(PAY_CHANNEL_TYPE_WECHAT);
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getPaymentChannelType(), orders.get(0).getPaymentChannelType());
    }

    @Test
    void getOrderListByPaymentStatusTest_success() throws Exception {
        Order testOrder = new Order();
        testOrder.setPaymentStatus(PAY_PENDING);
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getPaymentStatus(), orders.get(0).getPaymentStatus());
    }

    @Test
    void getOrderListByGrantStatusTest_success() throws Exception {

        Order testOrder = new Order();
        testOrder.setGrantStatus(GRANT_PENDING);
        String orderJson = JSONObject.toJSONString(testOrder);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertEquals(testOrder.getGrantStatus(), orders.get(0).getGrantStatus());
    }

    @Test
    void getOrderListByTimeRangeTest_success() throws Exception {

        Integer paymentStatus = PAY_SUCCESS;
        String paymentNo3rd = PAYMENTNO3RD;
        Integer paymentChannelType = PAY_CHANNEL_TYPE_WECHAT;
        Long paymentChannelId = 1l;
        orderService.updatePaymentStatus(randomOrder.getId(), paymentStatus, paymentNo3rd, paymentChannelType, paymentChannelId);

        Order testOrder = new Order();
        testOrder.setId(randomOrder.getId());
        testOrder.setOrderStartTime(new Date(System.currentTimeMillis() - 3600000)); // 1小时前
        testOrder.setOrderEndTime(new Date());
        String orderJson = JSONObject.toJSONString(testOrder);
        System.out.println(orderJson);

        MvcResult result = mockMvc.perform(get("/v1/order/list")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Map<String, Object>>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Order> orders = new ObjectMapper().convertValue(responseData.getData().get("orders"), new TypeReference<List<Order>>() {});
        assertNotNull(orders.get(0).getOrderTime());
    }

    @Test
    void updateUserPaymentStatusSuccessTest_success() throws Exception {
        Integer paymentStatus = PAY_SUCCESS;
        String paymentNo3rd = PAYMENTNO3RD;
        Integer paymentChannelType = PAY_CHANNEL_TYPE_WECHAT;
        Long paymentChannelId = 1l;

        mockMvc.perform(post("/v1/order/payment")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("paymentStatus", String.valueOf(paymentStatus))
                        .param("paymentNo3rd", paymentNo3rd)
                        .param("paymentChannelType", String.valueOf(paymentChannelType))
                        .param("paymentChannelId", String.valueOf(paymentChannelId))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(PAY_SUCCESS, order.getPaymentStatus());
        assertNotNull(order.getPaymentTime());
        assertNotNull(PAY_CHANNEL_TYPE_WECHAT, String.valueOf(order.getPaymentChannelType()));
    }

    @Test
    void updateUserPaymentStatusFailedTest_success() throws Exception {
        Integer paymentStatus = PAY_FAILED;

        mockMvc.perform(post("/v1/order/payment")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("paymentStatus", String.valueOf(paymentStatus))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(PAY_FAILED, order.getPaymentStatus());
    }

    @Test
    void updateUserPaymentStatusOvertimeTest_success() throws Exception {
        Integer paymentStatus = PAY_OVERTIME;

        mockMvc.perform(post("/v1/order/payment")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("paymentStatus", String.valueOf(paymentStatus))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(PAY_OVERTIME, order.getPaymentStatus());
    }

    @Test
    void updateUserGrantStatusSuccessTest_success() throws Exception {

        mockMvc.perform(post("/v1/order/grant")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("grantStatus", String.valueOf(GRANT_SUCCESS))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(GRANT_SUCCESS, order.getGrantStatus());
        assertNotNull(order.getGrantTime());
    }

    @Test
    void userGrantFailedTest_success() throws Exception {

        mockMvc.perform(post("/v1/order/grant")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("grantStatus", String.valueOf(GRANT_FAILED))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(GRANT_FAILED, order.getGrantStatus());
    }

    @Test
    void updateUserCommentTest_success() throws Exception {
        String userComment = "test comment";

        mockMvc.perform(post("/v1/order/user-comment")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("userComment", userComment)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(userComment, order.getUserComment());
    }

    @Test
    void updateAdminCommentTest_success() throws Exception {
        String adminComment = "test comment";
        mockMvc.perform(post("/v1/order/admin-comment")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .param("adminComment", adminComment)
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertNotNull(order);
        assertEquals(adminComment, order.getAdminComment());
    }

    @Test
    void logicalDeleteOrderTest_success() throws Exception {
        mockMvc.perform(post("/v1/order/delete")
                        .param("id", String.valueOf(randomOrder.getId()))
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Order order = orderService.getOrderById(randomOrder.getId());
        assertEquals(1, order.getIsDeleted());
    }
}
