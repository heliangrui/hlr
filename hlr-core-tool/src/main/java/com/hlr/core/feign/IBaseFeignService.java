package com.hlr.core.feign;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IBaseFeignService
 * Description:
 * date: 2023/11/23 16:02
 *
 * @author hlr
 */
public interface IBaseFeignService {
    @RequestMapping("/actuator/health")
    String health();
}
