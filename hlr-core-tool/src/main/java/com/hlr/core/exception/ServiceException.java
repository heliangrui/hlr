package com.hlr.core.exception;

import com.hlr.core.config.EnumConfig;

/**
 * ServiceException
 * Description:
 * date: 2023/11/23 15:31
 *
 * @author hlr
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;
    // 错误编码
    private EnumConfig.R_CODE rCode;
    // 错误信息
    private String errorMessage;
    // 错误信息 title
    private String errorCode;

    public ServiceException(EnumConfig.R_CODE code) {
        this.rCode = code;
    }

    public ServiceException(EnumConfig.R_CODE code, String message) {
        super(message);
        this.errorMessage = message;
        this.rCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
