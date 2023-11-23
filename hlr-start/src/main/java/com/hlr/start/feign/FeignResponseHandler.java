package com.hlr.start.feign;

import com.hlr.core.bean.JsonResponse;
import com.hlr.core.config.EnumConfig;
import com.hlr.core.exception.ServiceException;

/**
 * FeignResponseHandler
 * Description:
 * date: 2023/11/23 16:08
 *
 * @author hlr
 */
public class FeignResponseHandler {
    public static JsonResponse check(JsonResponse response) throws ServiceException {
        if (response.getCode() != EnumConfig.R_CODE.OK) {
            ServiceException serviceException = null;
            if (response.getData() instanceof String) {
                if (((String) response.getData()).contains("NormalWarnException")) {
                    serviceException = new ServiceException(response.getCode(), getFeignMessage(response.getData()));
                } else if (((String) response.getData()).contains("MsaBlockedException")) {
                    serviceException = new ServiceException(response.getCode(), getFeignMessage(response.getData()));
                }
            }
            if (serviceException == null) {
                serviceException = new ServiceException(response.getCode(), getFeignMessage(response.getData()));
            }
            serviceException.setErrorCode(response.getErrorMsg());
            throw serviceException;
        } else {
            return response;
        }
    }

    private static String getFeignMessage(Object data) {
        String message = null;
        if (data instanceof String) {
            message = (String) data;
        }

        StringBuilder sb = new StringBuilder("feign error->>");
        String[] lines = message.split("\n\t");

        for (int i = 0; i < Math.min(lines.length, 2); ++i) {
            sb.append(lines[i]);
            sb.append("-》");
        }

        return sb.toString();
    }
}
