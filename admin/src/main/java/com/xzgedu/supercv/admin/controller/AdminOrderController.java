package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.exception.PaymentException;
import com.xzgedu.supercv.common.exception.UserNotFoundException;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.OrderFilter;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.enums.GrantStatus;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import com.xzgedu.supercv.order.service.OrderNoGenerator;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.order.service.PaymentChannelService;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="订单管理")
@RequestMapping("/admin/order")
@RestController
public class AdminOrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询订单")
    @GetMapping("/list")
    public Map<String, Object> getOrders(@RequestParam(value = "order_no", required = false) String orderNo,
                                         @RequestParam(value = "product_id", required = false) Long productId,
                                         @RequestParam(value = "payment_channel_id", required = false) Long paymentChannelId,
                                         @RequestParam(value = "payment_status", required = false) Integer paymentStatus,
                                         @RequestParam(value = "grant_status", required = false) Integer grantStatus,
                                         @RequestParam(value = "uid", required = false) Long uid,
                                         @RequestParam(value = "start_time", required = false) Long startTime,
                                         @RequestParam(value = "end_time", required = false) Long endTime,
                                         @RequestParam("page_no") int pageNo,
                                         @RequestParam("page_size") int pageSize) {
        if (pageNo <= 0) pageNo = 1;
        if (pageSize < 10) pageSize = 10;
        if (pageSize > 100) pageSize = 100;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setOrderNo(orderNo);
        orderFilter.setProductId(productId);
        orderFilter.setPaymentChannelId(paymentChannelId);
        orderFilter.setPaymentStatus(paymentStatus);
        orderFilter.setGrantStatus(grantStatus);
        orderFilter.setUid(uid);
        if (startTime != null) {
            orderFilter.setStartTime(new Date(startTime));
        }
        if (endTime != null) {
            orderFilter.setEndTime(new Date(endTime));
        }

        List<Order> orders = orderService.getOrders(orderFilter, limitOffset, limitSize);
        int count = orderService.countOrders(orderFilter);
        Map<String, Object> res = new HashMap<>();
        res.put("orders", orders);
        res.put("count", count);
        return res;
    }

    @Operation(summary = "创建订单并授权")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/create")
    public void createOrderAndGrant(@RequestParam(value = "product_id") Long productId,
                                    @RequestParam("payment_channel_id") Long paymentChannelId,
                                    @RequestParam("payment_amount") BigDecimal paymentAmount,
                                    @RequestParam("telephone") String telephone,
                                    @RequestParam(value = "admin_comment", required = false) String adminComment)
            throws GenericBizException, UserNotFoundException, PaymentException {
        Order order = new Order();
        User user = userService.getUserByTelephone(telephone);
        if (user == null) throw new UserNotFoundException("User not found: " + telephone);
        Long uid = user.getId();
        order.setOrderNo(OrderNoGenerator.generateOrderNo(uid));
        order.setProductId(productId);
        Product product = productService.getProductById(productId);
        if (product != null) {
            order.setProductName(product.getName());
        }

        PaymentChannel paymentChannel = paymentChannelService.getPaymentChannelById(paymentChannelId);
        order.setPaymentChannelId(paymentChannel.getId());
        order.setPaymentChannelName(paymentChannel.getName());
        order.setPaymentAmount(paymentAmount);
        order.setPaymentTime(new Date());
        order.setPaymentStatus(PaymentStatus.PAID.getValue());
        order.setGrantStatus(GrantStatus.PENDING.getValue());
        order.setUid(uid);
        order.setAdminComment(adminComment);
        if (!orderService.addOrder(order)) {
            throw new GenericBizException("Failed to create order: " + order.toString());
        }
        orderService.grant(uid, order.getId(), productId);
        orderService.updateGrantStatus(order.getId(), GrantStatus.SUCCESS.getValue());
    }
}
