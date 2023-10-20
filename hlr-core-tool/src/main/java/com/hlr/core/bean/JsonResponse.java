package com.hlr.core.bean;

import com.hlr.core.config.EnumConfig;

/**
 * JsonResponse
 * Description:
 * date: 2023/10/20 11:13
 *
 * @author hlr
 */
public class JsonResponse<T> {
    private T data;

    private String traceId;

    private EnumConfig.R_CODE code;

    private String errorMsg;

    public JsonResponse() {
        this.code = EnumConfig.R_CODE.OK;
    }

    public JsonResponse(T data) {
        this.code = EnumConfig.R_CODE.OK;
        this.data = data;
    }

    public JsonResponse(EnumConfig.R_CODE rCode, String errorMsg) {
        this.code = rCode;
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public EnumConfig.R_CODE getCode() {
        return code;
    }

    public void setCode(EnumConfig.R_CODE code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
