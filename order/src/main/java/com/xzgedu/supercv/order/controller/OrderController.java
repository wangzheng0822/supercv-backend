package com.xzgedu.supercv.order.controller;

import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.exception.OrderNotFoundException;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "订单信息接口")
@RestController
@RequestMapping("/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "根据订单id获取订单信息")
    @GetMapping(path = "/info")
    public Order getOrderInfo(@RequestParam("id") long id) throws OrderNotFoundException {
        Order order = orderService.getOrderById(id);
        //TODO 填充产品名称
        if (Objects.isNull(order)) {
            throw new OrderNotFoundException("Order not found: [id=" + id + "]");
        }
        return order;
    }

    @Operation(summary = "根据订单号获取订单信息")
    @GetMapping(path = "/query")
    public Order getOrderInfoByOrderNo(@RequestParam("orderNo") String orderNo) throws OrderNotFoundException {
        Order order = orderService.getOrderByOrderNo(orderNo);
        //TODO 填充产品名称
        if (Objects.isNull(order)) {
            throw new OrderNotFoundException("Order not found: [orderNo=" + orderNo + "]");
        }
        return order;
    }

    @Operation(summary = "用户分页查询订单")
    @GetMapping(path = "/list_by_uid")
    public Map<String, Object> listOrdersByUserId(@RequestParam Long userId, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        List<Order> orders = orderService.listOrdersByUserId(userId, limitOffset, limitSize);
        int count = orderService.countOrderByUserid(userId);
        orders = orderService.batchFillInProductName(orders);

        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("orders", orders);

        return resp;
    }

    @Operation(summary = "分页查询订单")
    @GetMapping(path = "/list")
    public Map<String, Object> listOrders(@RequestBody Order order, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("limitOffset", limitOffset);
        params.put("limitSize", limitSize);
        params.put("order", order);
        List<Order> orders = orderService.listOrdersByCondition(params);
        int count = orderService.countOrderByCondition(params);

        orders = orderService.batchFillInProductName(orders);

        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("orders", orders);

        return resp;
    }

    @Operation(summary = "创建订单")
    @PostMapping(path = "/create")
    public String addOrder(@RequestBody Order order) throws GenericBizException {
        // 检查用户ID是否存在
        if (Objects.isNull(order.getUserId())) {
            throw new GenericBizException("User id is required");
        }

        //检查产品ID是否存在
        if (Objects.isNull(order.getProductId())) {
            throw new GenericBizException("Product id is required");
        }

        // 生成订单号
        String orderNo = orderService.generateOrderNo(order.getUserId());
        order.setOrderNo(orderNo);

        if (!orderService.addOrder(order)) {
            throw new GenericBizException("Failed to create order");
        }

        return orderNo;
    }

    @PostMapping("/payment")
    @Operation(summary = "更新订单支付状态")
    public void updateOrderPayment(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "paymentStatus", required = true) Integer paymentStatus,
            @RequestParam(value = "paymentNo3rd", required = false) String paymentNo3rd,
            @RequestParam(value = "paymentChannelType", required = false) Integer paymentChannelType,
            @RequestParam(value = "paymentChannelId", required = false) Long paymentChannelId
    ) throws GenericBizException {
        if (Objects.isNull(id) || Objects.isNull(paymentStatus)) {
            throw new GenericBizException("id and paymentStatus are required");
        }

        if (!orderService.updatePaymentStatus(id, paymentStatus, paymentNo3rd, paymentChannelType, paymentChannelId)) {
            throw new GenericBizException("Failed to update payment status");
        }
    }

    @PostMapping("/grant")
    @Operation(summary = "更新订单权益状态")
    public void updateOrderGrant(@RequestParam("id") Long id, @RequestParam("grantStatus") Integer grantStatus) throws GenericBizException {
        if (Objects.isNull(id) || Objects.isNull(grantStatus)) {
            throw new GenericBizException("id and grantStatus are required");
        }

        if (!orderService.updateGrantStatus(id, grantStatus)) {
            throw new GenericBizException("Failed to update grant status");
        }
    }

    @PostMapping("/user-comment")
    @Operation(summary = "更新用户评价")
    public void updateUserComment(@RequestParam("id") Long id, @RequestParam("userComment") String userComment) throws GenericBizException {
        if (Objects.isNull(id) || Objects.isNull(userComment)) {
            throw new GenericBizException("id and userComment are required");
        }

        if (!orderService.updateUserComment(id, userComment)) {
            throw new GenericBizException("Failed to update user comment");
        }
    }

    @PostMapping("/admin-comment")
    @Operation(summary = "更新管理员评价")
    public void updateAdminComment(@RequestParam("id") Long id, @RequestParam("adminComment") String adminComment) throws GenericBizException {
        if (Objects.isNull(id) || Objects.isNull(adminComment)) {
            throw new GenericBizException("id and adminComment are required");
        }

        if (!orderService.updateAdminComment(id, adminComment)) {
            throw new GenericBizException("Failed to update admin comment");
        }
    }

    @Operation(summary = "删除订单")
    @PostMapping(path = "/delete")
    public void deleteOrder(@RequestParam("id") long id) throws GenericBizException {
        if (!orderService.updateLogicDeletion(id)) {
            throw new GenericBizException("Failed to delete id: [id=" + id + "]");
        }
    }
}
