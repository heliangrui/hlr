package com.hlr.start.web;

import com.hlr.core.bean.JsonResponse;
import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * IGlobalWebHandler
 * Description:
 * date: 2023/10/20 11:46
 *
 * @author hlr
 */
public interface IGlobalWebHandler {
    Object onReturn(JsonResponse jsonResponse, ServerHttpRequest request);

    void onException(JsonResponse jsonResponse, HttpServletResponse response, Exception exception);
}
