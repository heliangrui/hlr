package com.hlr.start.utils;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MsaContextHolder
 * Description:
 * date: 2023/10/20 10:21
 *
 * @author hlr
 */
public class MsaContextHolder {

    // 用域记录当前线程下信息
    private static final ThreadLocal<Map<String, String>> holder = new NamedThreadLocal("Request attributes");

    public MsaContextHolder() {
    }

    public static String get(String key) {
        Map<String, String> kv = (Map) holder.get();
        return kv != null ? (String) kv.get(key) : null;
    }

    private static void set(String key, String value) {
        Map kv = (Map) holder.get();
        if (kv == null) {
            kv = new HashMap();
        }

        ((Map) kv).put(key, value);
        holder.set(kv);
    }

    public static void setTraceId() {
        set("traceId", UUID.randomUUID().toString());
        MDC.put("traceId", TraceUtil.getTraceId((HttpServletRequest) null));
    }

    public static void setTag(String tag) {
        set("tag", tag);
    }

    public static void setPlayload(String playload) {
        set("fhd-playload", playload);
    }

    public static void initTraceIdAndMdc(HttpServletRequest request) {
        String traceId = TraceUtil.getTraceId(request);
        if (traceId == null) {
            setTraceId();
        } else {
            MDC.put("traceId", traceId);
        }

    }

    public static void setDubboAttachemnt(HttpServletRequest request) {
        initTraceIdAndMdc(request);
        RpcContext.getClientAttachment().setAttachment("traceId", TraceUtil.getTraceId(request));
        RpcContext.getClientAttachment().setAttachment("tag", TraceUtil.getTag(request));
    }

}
