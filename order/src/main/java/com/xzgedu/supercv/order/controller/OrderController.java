package com.xzgedu.supercv.order.controller;

import com.xzgedu.supercv.common.exception.*;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.OrderFilter;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.enums.GrantStatus;
import com.xzgedu.supercv.order.enums.PaymentChannelType;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import com.xzgedu.supercv.order.payment.PaymentFactory;
import com.xzgedu.supercv.order.service.OrderNoGenerator;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.order.service.PaidCallbackService;
import com.xzgedu.supercv.order.service.PaymentChannelService;
import com.xzgedu.supercv.product.domain.Product;
import com.xzgedu.supercv.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "订单")
@Slf4j
@RequestMapping("/v1/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Autowired
    private PaidCallbackService paidCallbackService;

    @Autowired
    private PaymentFactory paymentFactory;

    @Value("${spring.profiles.active}")
    private String active;

    @Operation(summary = "创建订单")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestHeader("uid") long uid,
                                           @RequestParam("product_id") long productId,
                                           @RequestParam("payment_channel_type") Integer paymentChannelType,
                                           @RequestParam("final_price") BigDecimal finalPrice,
                                           @RequestParam(value = "user_comment", required = false) String userComment)
            throws GenericBizException, PaymentChannelDisabledException, ProductNotFoundException, OrderNotFoundException, PaymentException {
        //有黑客通过抓包工具，将final_price改成1元钱！PaymentException
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Failed to find product info: " + productId);
        }

        if (product.getDiscountPrice().compareTo(finalPrice) != 0) {
            log.error("黑客行为：订单支付金额被修改！[uid=" + uid + "]");
            throw new GenericBizException("黑客行为：订单支付金额被修改！[uid=" + uid + "]");
        }

        Order order = new Order();
        order.setOrderNo(OrderNoGenerator.generateOrderNo(uid));
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setOrderTime(new Date());
        PaymentChannel paymentChannel = paymentChannelService.getEnabledPaymentChannelByType(paymentChannelType);
        if (paymentChannel == null) {
            //支付渠道更新，请刷新重新！
            throw new PaymentChannelDisabledException("Payment channel is disabled for type: " + paymentChannelType);
        }
        order.setPaymentChannelId(paymentChannel.getId());
        order.setPaymentChannelType(paymentChannelType);
        order.setPaymentChannelName(paymentChannel.getName());
        order.setPaymentAmount(finalPrice);
        order.setPaymentStatus(PaymentStatus.PENDING.getValue());
        order.setGrantStatus(GrantStatus.PENDING.getValue());
        order.setUid(uid);
        order.setUserComment(userComment);
        if (orderService.addOrder(order) == false) {
            throw new GenericBizException("Failed to create order: " + order.toString());
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("orderNo", order.getOrderNo());

        // 支付金额为0，直接支付成功
        if (order.getPaymentAmount().compareTo(BigDecimal.ZERO) == 0) {
            paidCallbackService.completePayment(
                    order.getOrderNo(), null, new Date(), PaymentStatus.PAID);
            return resp;
        }

        String qrUrl = null;
        if (this.active.equals("dev") || this.active.equals("ut") || this.active.equals("test")) {
            qrUrl = paymentFactory.getPayment(PaymentChannelType.MOCK_PAYMENT).createPaymentQrUrl(
                    null, null, null, null, null);
            //开发环境无法回调，没法更新订单状态，因此，需要在这里主动更新一下（不管有没有支付，都更新为支付）
            paidCallbackService.completePayment(order.getOrderNo(), null, new Date(), PaymentStatus.PAID);
        } else if (this.active.equals("prod")) {
            qrUrl = paymentFactory.getPayment(PaymentChannelType.of(order.getPaymentChannelType()))
                    .createPaymentQrUrl(paymentChannel, order.getOrderNo(), order.getPaymentAmount(),
                            order.getProductName(), null);
        }

        // 将qrUrl写到数据库中,方便实现"继续支付"功能
        orderService.updateQrUrl(order.getId(), qrUrl);
        resp.put("qr", qrUrl);
        return resp;
    }

    @Operation(summary = "关闭订单")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/close")
    public void closeOrder(@RequestHeader("uid") long uid,
                           @RequestParam("no") String orderNo)
            throws GenericBizException, NoPermissionException {
        Order order = orderService.getOrderByNo(orderNo);
        if (order == null) {
            throw new GenericBizException("Failed to find order:" + orderNo);
        }

        if (order.getUid() != uid) {
            throw new NoPermissionException("No permission to close order of uid=" + order.getUid() + " by uid=" + uid);
        }

        //只有状态为待支付，才能更新为关闭！！！
        if (!orderService.closeOrderIfNotPaid(order.getId())) {
            throw new GenericBizException("Failed to update order status for order: " + order.getId());
        }
    }

    @Operation(summary = "根据订单号查询订单")
    @GetMapping("/detail")
    public Order getOrderByNo(@RequestHeader("uid") long uid,
                              @RequestParam("no") String no) {
        Order order = orderService.getOrderByNo(no);
        if (order == null) return null;
        if (order.getUid() != uid) {
            log.error("No permission to access order: " + order.getOrderNo() + " by uid: " + uid);
            return null;
        }
        return order;
    }

    @Operation(summary = "分页查询我的订单列表")
    @GetMapping("/list")
    public Map<String, Object> getMyOrders(@RequestHeader("uid") long uid,
                                           @RequestParam("page_no") int pageNo,
                                           @RequestParam("page_size") int pageSize) {
        if (pageNo <= 0) pageNo = 1;
        if (pageSize < 10) pageSize = 10;
        if (pageSize > 100) pageSize = 100;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setUid(uid);
        List<Order> orders = orderService.getOrders(orderFilter, limitOffset, limitSize);
        int count = orderService.countOrders(orderFilter);
        Map<String, Object> res = new HashMap<>();
        res.put("orders", orders);
        res.put("count", count);
        return res;
    }
}
