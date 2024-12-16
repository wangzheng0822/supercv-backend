package com.xzgedu.supercv.resume.mapper.sql;

import java.util.List;
import java.util.Map;

public class ResumeModuleItemProvider {
    public String selectResumeModuleItemsByModuleIds(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("select * from resume_module_item where 1=1 ");
        List<Long> moduleIds = (List<Long>) params.get("moduleIds");
        sb.append(" module_id in " + buildInFilter(moduleIds));
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
