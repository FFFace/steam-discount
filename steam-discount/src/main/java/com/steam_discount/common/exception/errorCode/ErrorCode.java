package com.steam_discount.common.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(400, "잘못된 요청", "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(400, "잘못된 요청", "로그인 중인 사용자가 아닙니다."),
    NOT_MATCH_PASSWORD(400, "잘못된 요청", "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_BOARD(400, "잘못된 요청", "존재하지 않는 게시판 입니다."),
    NOT_FOUND_POST(400, "잘못된 요청", "존재하지 않는 게시글 입니다."),
    NOT_VERIFY_EMAIL(403, "인증되지 않음", "아직 인증되지 않은 이메일입니다."),
    NOT_FOUND_ALGORITHM(500, "알고리즘 없음", "알고리즘을 찾을 수 없습니다."),
    ACCESS_TOKEN_EXPIRED(401, "토큰 만료", "엑세스 토큰이 만료되었습니다.");

    private final int errorCode;
    private final String errorName;
    private final String errorMessage;
}
