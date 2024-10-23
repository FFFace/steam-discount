package com.steam_discount.common.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(400, "잘못된 요청", "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(400, "잘못된 요청", "로그인 중인 사용자가 아닙니다."),
    NOT_MATCH_PASSWORD(400, "잘못된 요청", "비밀번호가 일치하지 않습니다."),
    NOT_MATCH_NEW_PASSWORD(400, "잘못된 요청", "변경할 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_BOARD(400, "잘못된 요청", "존재하지 않는 게시판 입니다."),
    ALREADY_EXIST_BOARD(400, "잘못된 요청", "이미 존재하는 게시판 입니다."),
    NOT_FOUND_POST(400, "잘못된 요청", "존재하지 않는 게시글 입니다."),
    NOT_FOUND_COMMENT(400, "잘못된 요청", "존재하지 않는 댓글 입니다."),
    ALREADY_USED_NICKNAME(400, "잘못된 요청", "이미 사용중인 별명입니다."),
    ACCESS_TOKEN_EXPIRED(401, "토큰 만료", "엑세스 토큰이 만료되었습니다."),
    NOT_VERIFY_EMAIL(403, "인증되지 않음", "아직 인증되지 않은 이메일입니다."),
    NOT_MATCH_USER_FOR_UPDATE_POST(403, "수정 권한 없음", "본인이 작성했던 게시글만 수정할 수 있습니다."),
    NOT_MATCH_USER_FOR_UPDATE_COMMENT(403, "수정 권한 없음", "본인이 작성했던 게시글만 수정할 수 있습니다."),
    DISABLE_POST(404, "비활성화된 게시글", "해당 게시글은 비활성화된 게시글 입니다."),
    NOT_FOUND_ALGORITHM(500, "알고리즘 없음", "알고리즘을 찾을 수 없습니다.");

    private final int errorCode;
    private final String errorName;
    private final String errorMessage;
}
