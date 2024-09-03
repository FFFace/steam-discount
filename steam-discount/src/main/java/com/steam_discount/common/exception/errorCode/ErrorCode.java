package com.steam_discount.common.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(401, "잘못된 요청", "해당 사용자를 찾을 수 없습니다.");


    private final int errorCode;
    private final String errorName;
    private final String errorMessage;
}
