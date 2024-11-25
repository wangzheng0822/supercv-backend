package com.xzgedu.supercv.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class AccessLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest  httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String apiInfo = buildApiInfo(httpRequest);
        String reqInfo = "[REQ] " + apiInfo;
        log.info(reqInfo);

        chain.doFilter(request, response);

        long costTime = System.currentTimeMillis() - startTime;
        StringBuilder respInfo = new StringBuilder();
        respInfo.append("[RESP] ");
        respInfo.append(costTime + "ms ");
        respInfo.append(httpResponse.getStatus() + " "+ apiInfo);
        log.info(respInfo.toString());
    }

    private String buildApiInfo(HttpServletRequest request) {
        StringBuilder apiInfo = new StringBuilder("");
        apiInfo.append(request.getMethod() + " " + request.getRequestURI());
        if (request.getMethod().equalsIgnoreCase("GET")) {
            String queryString = request.getQueryString();
            if (!StringUtils.isBlank(queryString)) {
                apiInfo.append("?" + queryString);
            }
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            Map<String, String[]> postDataMap = request.getParameterMap();
            if (postDataMap != null && !postDataMap.isEmpty()) {
                StringBuilder postDataStr = new StringBuilder();
                for (Map.Entry<String, String[]> entry : postDataMap.entrySet()) {
                    postDataStr.append("&" + entry.getKey() + "=");
                    String[] values = entry.getValue();
                    if (values != null && values.length > 0) {
                        StringBuilder valueStr = new StringBuilder();
                        for (int i = 0; i < values.length; ++i) {
                            if (values[i]!=null && values[i].length()>128) {
                                valueStr.append("," + values[i].substring(0, 128)
                                        + "...<" + values[i].length() + ">");
                            } else {
                                valueStr.append("," + values[i]);
                            }
                        }
                        postDataStr.append(valueStr.toString().substring(1));
                    }
                }

                apiInfo.append(" {" + postDataStr.toString().substring(1) + "}");
                request.setAttribute("post_data", postDataStr.toString().substring(1));
            }
        }
        apiInfo.append(" {token=" + request.getHeader("Authorization") + "}");
        return apiInfo.toString();
    }
}