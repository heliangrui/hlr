package com.hlr.start.feign;

import com.hlr.core.config.EnumConfig;
import com.hlr.core.exception.ServiceException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * FeignErrorDecoder
 * Description: feign 请求异常错误处理
 * date: 2023/11/23 15:21
 *
 * @author hlr
 */
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        StringBuilder sb = new StringBuilder("feign error->>");

        try {
            sb.append(" methodKey:");
            sb.append(s);
            sb.append(" response:");
            sb.append(Util.toString(response.body().asReader(Charset.forName("utf-8"))));
        } catch (IOException var5) {
        }

        ServiceException serviceException = new ServiceException(EnumConfig.R_CODE.CLOUD_FAIL, sb.toString());
        serviceException.setErrorCode("微服务请求失败");
        return serviceException;
    }
}
