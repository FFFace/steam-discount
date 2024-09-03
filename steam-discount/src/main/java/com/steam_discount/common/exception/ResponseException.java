package com.steam_discount.common.exception;

import com.steam_discount.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseException {

    private ErrorCode errorCode;
}
