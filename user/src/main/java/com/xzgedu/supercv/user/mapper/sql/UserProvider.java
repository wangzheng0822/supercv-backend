package com.xzgedu.supercv.user.mapper.sql;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserProvider {
    public String selectUsersByUids(List<Long> uids) {
        return "select * from cv_user where id in " + buildInFilter(uids);
    }

    public String countUsersByDuration(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("select count(*) from cv_user where 1=1 ");
        Date startTime = (Date) params.get("startTime");
        Date endTime = (Date) params.get("endTime");
        if (startTime != null) {
            sb.append(" and create_time >= #{startTime}");
        }
        if (endTime != null) {
            sb.append(" and create_time <= #{endTime}");
        }
        return sb.toString();
    }

    private String buildInFilter(List<Long> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (ids != null && !ids.isEmpty()) {
            for (int i = 0; i < ids.size() - 1; ++i) {
                sb.append(ids.get(i) + ", ");
            }
            sb.append(ids.get(ids.size() - 1));
        }
        sb.append(")");
        return sb.toString();
    }
}
