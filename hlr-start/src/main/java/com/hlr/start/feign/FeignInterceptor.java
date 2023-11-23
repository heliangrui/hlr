package com.hlr.start.feign;

import com.hlr.start.utils.MsaContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * FeignInterceptor
 * Description: feign 请求增加头部入参请求
 * date: 2023/11/23 15:39
 *
 * @author hlr
 */
public class FeignInterceptor implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        MsaContextHolder.setFeignAttachemnt(requestTemplate, getServletRequest(), appName);
    }

    private HttpServletRequest getServletRequest() {
        return RequestContextHolder.getRequestAttributes() == null ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
