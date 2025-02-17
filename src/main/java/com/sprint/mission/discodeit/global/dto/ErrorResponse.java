package com.sprint.mission.discodeit.global.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
	private final String errorCode;
	private final String message;
	private final HttpStatus status;
	private final LocalDateTime timestamp;

	// 잘못된 요청 응답 (400 BAD REQUEST)
	public static ErrorResponse badRequest(String message) {
		return new ErrorResponse("BAD_REQUEST", message, HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}

	// 인증 실패 응답 (401 UNAUTHORIZED)
	public static ErrorResponse unauthorized(String message) {
		return new ErrorResponse("UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED, LocalDateTime.now());
	}

	// 권한 없음 응답 (403 FORBIDDEN)
	public static ErrorResponse forbidden(String message) {
		return new ErrorResponse("FORBIDDEN", message, HttpStatus.FORBIDDEN, LocalDateTime.now());
	}

	// 리소스를 찾을 수 없음 응답 (404 NOT FOUND)
	public static ErrorResponse notFound(String message) {
		return new ErrorResponse("NOT_FOUND", message, HttpStatus.NOT_FOUND, LocalDateTime.now());
	}

	// 중복된 리소스 응답 (409 CONFLICT)
	public static ErrorResponse conflict(String message) {
		return new ErrorResponse("CONFLICT", message, HttpStatus.CONFLICT, LocalDateTime.now());
	}

	// 서버 내부 오류 응답 (500 INTERNAL SERVER ERROR)
	public static ErrorResponse serverError(String message) {
		return new ErrorResponse("SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
	}
}
