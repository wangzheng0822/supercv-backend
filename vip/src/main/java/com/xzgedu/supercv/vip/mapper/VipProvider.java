package com.xzgedu.supercv.vip.mapper;

import java.util.Date;
import java.util.Map;

public class VipProvider {
    public String countVips(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("select count(*) from vip where 1=1 ");
        Date startTime = (Date) params.get("startTime");
        Date endTime = (Date) params.get("endTime");
        if (startTime != null) {
            sb.append(" and update_time >= #{startTime}");
        }
        if (endTime != null) {
            sb.append(" and update_time <= #{endTime}");
        }
        return sb.toString();
    }
}
