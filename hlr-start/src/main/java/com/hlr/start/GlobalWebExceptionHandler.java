package com.hlr.start;

import com.alibaba.fastjson.JSONObject;
import com.hlr.core.bean.JsonResponse;
import com.hlr.core.cache.annotation.UnBoxingResponse;
import com.hlr.core.config.EnumConfig;
import com.hlr.start.config.HlrConfigProperties;
import com.hlr.start.utils.TraceUtil;
import com.hlr.start.web.IGlobalWebHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * GlobalWebExceptionHandler
 * Description:
 * date: 2023/10/20 10:54
 *
 * @author hlr
 */
@RestControllerAdvice
public class GlobalWebExceptionHandler implements ResponseBodyAdvice<Object> {

    private HlrConfigProperties hlrConfigProperties;

    @Autowired(required = false)
    private IGlobalWebHandler globalWebHandler;

    public GlobalWebExceptionHandler(HlrConfigProperties hlrConfigProperties) {
        this.hlrConfigProperties = hlrConfigProperties;
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public JsonResponse processException(HttpServletResponse response, Exception exception) {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        String errorMsg = "系统异常";
        JsonResponse result = new JsonResponse(EnumConfig.R_CODE.FAIL, errorMsg);
        onException(result, response, exception);

        return result;
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (!hlrConfigProperties.isResponseAutoBoxing()) {
            return false;
        } else {
            UnBoxingResponse annotation = returnType.getDeclaringClass().getAnnotation(UnBoxingResponse.class);
            if (annotation != null) {
                return false;
            } else {
                UnBoxingResponse annotationMethod = returnType.getMethod().getAnnotation(UnBoxingResponse.class);
                return annotationMethod == null;
            }
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object msaStime = ((ServletServerHttpRequest) request).getServletRequest().getAttribute(TraceUtil.MSA_START_TIME);
        if (msaStime != null) {
            response.getHeaders().add("endpoint-rt", String.valueOf(System.currentTimeMillis() - (Long) msaStime));
        }
        JsonResponse jsonResponse;

        if (body instanceof JsonResponse) {
            jsonResponse = (JsonResponse) body;
            jsonResponse.setTraceId(response.getHeaders().getFirst(TraceUtil.TRACE_ID));
        } else {
            jsonResponse = new JsonResponse(body);
            jsonResponse.setTraceId(response.getHeaders().getFirst(TraceUtil.TRACE_ID));
            if (selectedConverterType == StringHttpMessageConverter.class) {
                response.getHeaders().add("Content-Type", "application/json");
                return JSONObject.toJSONString(onReturn(jsonResponse, request));
            }
        }
        return onReturn(jsonResponse, request);
    }

    private Object onReturn(JsonResponse jsonResponse, ServerHttpRequest request) {
        if (this.globalWebHandler != null) {
            Object object = this.globalWebHandler.onReturn(jsonResponse, request);
            if (object != null) {
                return object;
            }
        }

        return jsonResponse;
    }

    private void onException(JsonResponse jsonResponse, HttpServletResponse response, Exception exception) {
        if (this.globalWebHandler != null) {
            this.globalWebHandler.onException(jsonResponse, response, exception);
        }

    }


}
