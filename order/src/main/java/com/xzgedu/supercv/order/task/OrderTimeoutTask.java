package com.xzgedu.supercv.order.task;

import com.xzgedu.supercv.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderTimeoutTask {
    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 */1 * * * ? ")//每分钟执行一次
    public void orderOverTime() {
        try {
            int updatedCount = orderService.batchUpdatePaymentStatusOverTime();
            log.info("订单超时检查任务执行完成，共处理{}笔超时订单", updatedCount);
        } catch (Exception e) {
            log.error("订单超时检查任务执行失败", e);
        }
    }
}
