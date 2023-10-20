package com.hlr.start.web;

import com.hlr.start.utils.MsaContextHolder;
import com.hlr.start.utils.TpsUtil;
import com.hlr.start.utils.TraceUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebTraceInterceptor
 * Description:
 * date: 2023/10/20 10:13
 *
 * @author hlr
 */
public class WebTraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 进行链路追踪 traceId
        MsaContextHolder.initTraceIdAndMdc(request);
        String traceId = TraceUtil.getTraceId(request);
        if (traceId != null) {
            response.setHeader(TraceUtil.TRACE_ID, traceId);
        }
        // 记录请求开始时间
        request.setAttribute(TraceUtil.MSA_START_TIME, System.currentTimeMillis());
        // 服务tps 请求数 追踪
        TpsUtil.inc();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
