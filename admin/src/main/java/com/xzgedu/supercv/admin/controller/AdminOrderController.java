package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.order.domain.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xzgedu.supercv.order.service.OrderService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "订单管理接口")
@RequestMapping("/admin/order")
@RestController
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "分页查询订单")
    @GetMapping("/list")
    public Map<String, Object> listOrders(@RequestBody Order order, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        if (pageNo != null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize != null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        java.util.Map<java.lang.String, java.lang.Object> params = new HashMap<>();
        params.put("limitOffset", limitOffset);
        params.put("limitSize", limitSize);
        params.put("order", order);
        List<Order> orders = orderService.listOrdersByCondition(params);
        int count = orderService.countOrderByCondition(params);

        //TODO 填充产品名称
        orders = orderService.batchFillInProductName(orders);

        Map<java.lang.String, java.lang.Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("orders", orders);

        return resp;
    }
}
