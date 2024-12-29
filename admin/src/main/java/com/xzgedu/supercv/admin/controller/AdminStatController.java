package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.order.domain.OrderFilter;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import com.xzgedu.supercv.order.service.OrderService;
import com.xzgedu.supercv.user.service.UserService;
import com.xzgedu.supercv.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin/stat")
@RestController
public class AdminStatController {

    @Autowired
    private UserService userService;

    @Autowired
    private VipService vipService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public Map<String, Object> getAllStats() throws ParseException {
        Map<String, Object> stats = new HashMap<>();

        //统计用户
        int totalUserCount = userService.countUsersByDuration(null, null);
        int yesterdayUserCount = userService.countUsersByDuration(yesterday()[0], yesterday()[1]);
        int todayUserCount = userService.countUsersByDuration(today()[0], today()[1]);
        int weekUserCount = userService.countUsersByDuration(thisWeek()[0], thisWeek()[1]);
        int monthUserCount = userService.countUsersByDuration(thisMonth()[0], thisMonth()[1]);
        stats.put("totalUserCount", totalUserCount);
        stats.put("yesterdayUserCount", yesterdayUserCount);
        stats.put("todayUserCount", todayUserCount);
        stats.put("weekUserCount", weekUserCount);
        stats.put("monthUserCount", monthUserCount);

        //统计会员
        int totalVipCount = vipService.countVips(null, null);
        int yesterdayVipCount = vipService.countVips(yesterday()[0], yesterday()[1]);
        int todayVipCount = vipService.countVips(today()[0], today()[1]);
        int weekVipCount = vipService.countVips(thisWeek()[0], thisWeek()[1]);
        int monthVipCount = vipService.countVips(thisMonth()[0], thisMonth()[1]);

        stats.put("totalVipCount", totalVipCount);
        stats.put("yesterdayVipCount", yesterdayVipCount);
        stats.put("todayVipCount", todayVipCount);
        stats.put("weekVipCount", weekVipCount);
        stats.put("monthVipCount", monthVipCount);

        //统计订单
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setPaymentStatus(PaymentStatus.PAID.getValue());
        int totalAmount = orderService.sumOrderAmount(orderFilter);

        orderFilter.setStartTime(yesterday()[0]);
        orderFilter.setEndTime(yesterday()[1]);
        int yesterdayAmount = orderService.sumOrderAmount(orderFilter);

        orderFilter.setStartTime(today()[0]);
        orderFilter.setEndTime(today()[1]);
        int todayAmount = orderService.sumOrderAmount(orderFilter);

        orderFilter.setStartTime(thisWeek()[0]);
        orderFilter.setEndTime(thisWeek()[1]);
        int weekAmount = orderService.sumOrderAmount(orderFilter);

        orderFilter.setStartTime(thisMonth()[0]);
        orderFilter.setEndTime(thisMonth()[1]);
        int monthAmount = orderService.sumOrderAmount(orderFilter);

        stats.put("totalIncome", totalAmount);
        stats.put("yesterdayIncome", yesterdayAmount);
        stats.put("todayIncome", todayAmount);
        stats.put("weekIncome", weekAmount);
        stats.put("monthIncome", monthAmount);

        return stats;
    }

    private Date[] today() throws ParseException {
        LocalDateTime now = LocalDateTime.now(); // 获取当前日期和时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = now.withHour(0).withMinute(0).withSecond(0).withNano(0).format(formatter); // 设置为当天的0点
        String endTimeStr = now.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999).format(formatter); // 设置为当天的最后一分钟
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse(startTimeStr);
        Date endTime = sdf.parse(endTimeStr);
        return new Date[]{startTime, endTime};
    }

    public Date[] yesterday() throws ParseException {
        // 创建 Calendar 对象并设置为当前日期
        Calendar calendar = Calendar.getInstance();
        // 将日期调整到昨天
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        // 格式化日期输出
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = dateFormat.format(calendar.getTime()) + " 00:00:00";
        String endTimeStr = dateFormat.format(calendar.getTime()) + " 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse(startTimeStr);
        Date endTime = sdf.parse(endTimeStr);
        return new Date[]{startTime, endTime};
    }

    public static Date[] thisWeek() {
        Calendar calendar = Calendar.getInstance();

        // 获取当前是周几（Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ...）
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 如果当前不是周一，则调整到本周的周一
        if (currentDayOfWeek != Calendar.MONDAY) {
            // Calendar.DAY_OF_WEEK - Calendar.MONDAY 给出当前是周几相对于周一的偏移量
            // 注意：如果当前是周日，偏移量将是-1（因为Calendar.SUNDAY=1且Calendar.MONDAY=2），但我们想要的是6天的偏移
            int daysUntilMonday = (currentDayOfWeek - Calendar.MONDAY + 7) % 7;
            calendar.add(Calendar.DAY_OF_MONTH, -daysUntilMonday);
        }

        // 设置周一的时间为当天的开始
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfWeek = calendar.getTime();

        // 加上6天来到本周日
        calendar.add(Calendar.DAY_OF_MONTH, 6);

        // 设置周日的时间为当天的结束（23:59:59）
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfWeek = calendar.getTime();

        return new Date[]{startOfWeek, endOfWeek};
    }

    public static Date[] thisMonth() {
        Calendar calendar = Calendar.getInstance();

        // 设置为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 设置为当天的开始时间
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfMonth = calendar.getTime();

        // 设置为本月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        // 设置时间为当月的最后一天的23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfMonth = calendar.getTime();

        return new Date[]{startOfMonth, endOfMonth};
    }

}
