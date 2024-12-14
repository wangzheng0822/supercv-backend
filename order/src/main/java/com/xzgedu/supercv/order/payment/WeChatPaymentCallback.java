package com.xzgedu.supercv.order.payment;

import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.order.service.PaidCallbackService;
import com.xzgedu.supercv.order.service.PaymentChannelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Date;

@Slf4j
@RestController
public class WeChatPaymentCallback {

    @Autowired
    private PaidCallbackService paidCallbackService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentChannelService payChannelService;

    @Autowired
    private WeChatPayment weChatPayment;

    @Operation(summary = "支付完成回调接口")
    @PostMapping("/v1/order/paid/wx")
    public void completePayment(HttpServletRequest request,
                                HttpServletResponse resp,
                                @RequestBody String requestBody) throws IOException {
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(weChatPayment.config);

        // 构造 RequestParam
        String wechatPaySerial = request.getHeader("Wechatpay-Serial");
        String wechatSignature = request.getHeader("Wechatpay-Signature");
        String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
        String wehatTimestamp = request.getHeader("Wechatpay-Timestamp");
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(wechatPaySerial)
                .nonce(wechatpayNonce)
                .signature(wechatSignature)
                .timestamp(wehatTimestamp)
                .body(requestBody)
                .build();

        Transaction transaction = null;
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
             transaction = parser.parse(requestParam, Transaction.class);
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("wechat pay sign verification failed", e);
            writeResp(resp, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String orderNo = transaction.getOutTradeNo();
        String orderNo3rd = transaction.getTransactionId();
        String payTime = transaction.getSuccessTime();
        double payAmount = transaction.getAmount().getTotal() / 100.0f;
        boolean success = transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS);
        Order order = orderService.getOrderByNo(orderNo);
        PaymentChannel payChannel = payChannelService.getPaymentChannelById(order.getPaymentChannelId());
        if (payChannel == null) {
            log.error("Failed to find pay channel for order: " + orderNo);
            writeResp(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(payTime);
            Date date = Date.from(offsetDateTime.toInstant());
            PaymentStatus payStatus = success ? PaymentStatus.PAID : PaymentStatus.FAILED;
            if (payStatus.equals(PaymentStatus.FAILED)) {
                log.error("Wechat pay callback resp code is FAILURE: " + orderNo);
            }

            //有黑客通过抓包工具，修改支付的二维码，改成1元钱，支付，回调到这里要再检查一下，支付金额是否对！
            if (Math.abs(payAmount - order.getPaymentAmount().doubleValue()) > 1) {
                log.error("黑客行为：修改支付二维码 order no: " + orderNo);
                payStatus = PaymentStatus.FAILED;
            }
            paidCallbackService.completePayment(orderNo, orderNo3rd, date, payStatus);
            writeResp(resp, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error("Failed to execute paid callback logic.", e);
            writeResp(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void writeResp(HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print("SUCCESS");
    }
}
