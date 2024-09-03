package com.steam_discount.common.exception;

import com.steam_discount.common.exception.errorCode.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getErrorName());
        this.errorCode = errorCode;
    }

    public ResponseException toResponse(){
        return new ResponseException(errorCode);
    }
}
