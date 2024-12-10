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
    public Order getOrderInfo(@RequestParam("oid") long oid) throws OrderNotFoundException {
        Order order = orderService.getOrderById(oid);
        //TODO 填充产品名称
        if (Objects.isNull(order)) {
            throw new OrderNotFoundException("Order not found: [oid=" + oid + "]");
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

    @Operation(summary = "更新订单")
    @PostMapping(path = "/update")
    public void updateOrder(@RequestBody Order order) throws GenericBizException {
        // 校验订单号
        String orderNo = order.getOrderNo();
        if (Objects.isNull(orderNo)) {
            throw new GenericBizException("订单号不能为空");
        }

        try {
            // 更新支付状态
            updatePaymentStatusIfNeeded(order);

            // 更新权益状态
            updateGrantStatusIfNeeded(order);

            // 更新用户评价
            updateUserCommentIfNeeded(order);

            // 更新管理员评价
            updateAdminCommentIfNeeded(order);

        } catch (Exception e) {
            throw new GenericBizException("更新订单失败: " + e.getMessage());
        }
    }

    private void updatePaymentStatusIfNeeded(Order order) throws GenericBizException {
        Integer paymentStatus = order.getPaymentStatus();
        if (paymentStatus != null && !orderService.updatePaymentStatus(order.getOrderNo(), order)) {
            throw new GenericBizException(String.format("更新支付状态失败: [orderNo=%s, paymentStatus=%d]",
                    order.getOrderNo(), paymentStatus));
        }
    }

    private void updateGrantStatusIfNeeded(Order order) throws GenericBizException {
        Integer grantStatus = order.getGrantStatus();
        if (grantStatus != null && !orderService.updateGrantStatus(order.getOrderNo(), grantStatus)) {
            throw new GenericBizException(String.format("更新权益状态失败: [orderNo=%s, grantStatus=%d]",
                    order.getOrderNo(), grantStatus));
        }
    }

    private void updateUserCommentIfNeeded(Order order) throws GenericBizException {
        String userComment = order.getUserComment();
        if (userComment != null && !orderService.updateUserComment(order.getOrderNo(), userComment)) {
            throw new GenericBizException(String.format("更新用户评价失败: [orderNo=%s]", order.getOrderNo()));
        }
    }

    private void updateAdminCommentIfNeeded(Order order) throws GenericBizException {
        String adminComment = order.getAdminComment();
        if (adminComment != null && !orderService.updateAdminComment(order.getOrderNo(), adminComment)) {
            throw new GenericBizException(String.format("更新管理员评价失败: [orderNo=%s]", order.getOrderNo()));
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
