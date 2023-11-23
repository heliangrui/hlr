package com.hlr.start.feign;

import com.hlr.core.bean.JsonResponse;
import com.hlr.core.exception.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * FeignResponseAspect
 * Description:
 * date: 2023/11/23 15:58
 *
 * @author hlr
 */
@Aspect
public class FeignResponseAspect {
    
    @AfterReturning(pointcut = "execution(* com.hlr.core.feign.IBaseFeignService+.*(..))", returning = "returnValue")
    public void check(JoinPoint joinPoint, Object returnValue) throws ServiceException {
        if (returnValue != null && returnValue instanceof JsonResponse) {
            FeignResponseHandler.check((JsonResponse) returnValue);
        }
        
    }
}
