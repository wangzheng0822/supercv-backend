package com.xzgedu.supercv.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 慢SQL日志记录
 * @author wangzheng
 */
@Slf4j
@Component
@Intercepts(value = {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MyBatisInterceptor implements Interceptor {
    private static final Logger slowSqlLog = LoggerFactory.getLogger("MyBatisInterceptor.SlowSqlLog");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        BoundSql sql = statement.getBoundSql(invocation.getArgs()[1]);

        // 解析SQL参数
        List<ParameterMapping> list = sql.getParameterMappings();
        OgnlContext context = (OgnlContext) Ognl.createDefaultContext(sql.getParameterObject());
        List<Object> params = new ArrayList<>(list.size());
        for (ParameterMapping mapping : list) {
            Object value = Ognl.getValue(Ognl.parseExpression(mapping.getProperty()), context, context.getRoot());
            if (value instanceof String) {
                String valueStr = (String) value;
                if (valueStr.length() > 128) {
                    params.add(valueStr.substring(0, 128) + "...<" + valueStr.length() + ">");
                } else {
                    params.add(valueStr);
                }
            } else {
                params.add(value);
            }
        }

        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            if (costTime> 300) {
                slowSqlLog.warn("[SLOW SQL] " + costTime + "ms " + sql.getSql()
                        + " " + params.toString());
            } else {
                log.info("[SQL] " + costTime + "ms " + sql.getSql()
                        + " " + params.toString());
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}