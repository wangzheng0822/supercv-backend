package com.xzgedu.supercv.order.service;

import com.xzgedu.supercv.common.exception.PaymentException;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.OrderFilter;
import com.xzgedu.supercv.order.enums.GrantStatus;
import com.xzgedu.supercv.order.repo.OrderRepo;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import com.xzgedu.supercv.vip.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private VipService vipService;

    @Autowired
    private OrderRepo orderRepo;

    public boolean addOrder(Order order) {
        return orderRepo.addOrder(order);
    }

    public boolean deleteOrder(long id) {
        return orderRepo.deleteOrder(id);
    }

    public boolean updatePaymentStatus(long id, int paymentStatus) {
        return orderRepo.updatePaymentStatus(id, paymentStatus, new Date());
    }

    public boolean closeOrderIfNotPaid(long id) {
        return orderRepo.closeOrderIfNotPaid(id);
    }

    public boolean updateQrUrl(long id, String url) {
        return orderRepo.updateQrUrl(id, url);
    }

    public boolean updatePaidInfo(String orderNo, String orderNo3nd, Date paymentTime, int paymentStatus) {
        return orderRepo.updatePaidInfo(orderNo, orderNo3nd, paymentTime, paymentStatus);
    }

    public boolean updateGrantStatus(long id, int grantStatus) {
        return orderRepo.updateGrantStatus(id, grantStatus);
    }

    public Order getOrderById(long id) {
        return orderRepo.getOrderById(id);
    }

    public Order getOrderByNo(String no) {
        return orderRepo.getOrderByNo(no);
    }

    public List<Order> getOrders(OrderFilter orderFilter, int limitOffset, int limitSize) {
        return fillViewData(orderRepo.getOrders(orderFilter, limitOffset, limitSize));
    }

    private List<Order> fillViewData(List<Order> orders) {
        if (orders == null || orders.isEmpty()) return orders;
        List<Product> products = productService.getAllProducts();
        Map<Long, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        for (Order order : orders) {
            Product product = productMap.get(order.getProductId());
            if (product != null) {
                order.setProductName(product.getName());
            }
        }
        return orders;
    }

    public int countOrders(OrderFilter orderFilter) {
        return orderRepo.countOrders(orderFilter);
    }

    public BigDecimal sumOrderAmount(OrderFilter orderFilter) {
        return orderRepo.sumOrderAmount(orderFilter);
    }

    public void grant(long uid, long orderId, long productId) throws PaymentException {
        Product product = productService.getProductById(productId);
        if (!vipService.renewVip(uid, product.getDurationDays(),
                product.getAiAnalysisNum(), product.getAiOptimizationNum())) {
            throw new PaymentException("Failed to renew VIP for user: " + uid);
        }
        if (!orderRepo.updateGrantStatus(orderId, GrantStatus.SUCCESS.getValue())) {
            throw new PaymentException("Failed to update grant_status for order:" + orderId);
        }
    }
}
