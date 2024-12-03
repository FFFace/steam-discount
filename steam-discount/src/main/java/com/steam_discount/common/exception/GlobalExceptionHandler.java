package com.steam_discount.common.exception;

import com.steam_discount.common.exception.errorCode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseException> customExceptionHandler(CustomException exception){
        ResponseException responseException = exception.toResponse();
        ErrorCode errorCode = responseException.getErrorCode();
        log.error("### ERROR!! CODE: {}        CODE NAME: {}        CODE MESSAGE: {}",
                    errorCode.getErrorCode(), errorCode.getErrorName(), errorCode.getErrorMessage());

        return new ResponseEntity<>(responseException, HttpStatusCode.valueOf(errorCode.getErrorCode()));
    }
}
