package com.hlr.start.utils;

import org.apache.dubbo.rpc.RpcContext;

import javax.servlet.http.HttpServletRequest;

/**
 * TraceUtil
 * Description: 请求头部内容
 * date: 2023/10/20 10:04
 *
 * @author hlr
 */
public class TraceUtil {

    public static final String TRACE_ID = "traceId";

    public static final String TAG_SC = "tag";

    public static final String MSA_START_TIME = "msa_stime";


    public static String getTraceId(HttpServletRequest request) {
        return getTrace(TRACE_ID, request);
    }

    public static String getTag(HttpServletRequest request) {
        return getTrace(TAG_SC, request);
    }

    private static String getTrace(String key, HttpServletRequest request) {
        String trace = null;
        if (request != null) {
            trace = request.getHeader(key);
        }

        if (trace == null) {
            trace = getTraceFromDubbo(key);
        }

        if (trace == null) {
            trace = MsaContextHolder.get(key);
        }

        return trace;
    }

    private static String getTraceFromDubbo(String key) {
        return RpcContext.getServerAttachment().getAttachment(key);
    }

}
