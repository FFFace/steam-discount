package com.steam_discount.common.exception;

import com.steam_discount.common.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getErrorName());
        this.errorCode = errorCode;
    }
}
