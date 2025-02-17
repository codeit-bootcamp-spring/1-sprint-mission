package com.sprint.mission.discodeit.global.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonResponse<T> {
	private final String resultCode;
	private final String message;
	private final T data;
	private final HttpStatus httpStatus;
	private final LocalDateTime timestamp = LocalDateTime.now();

	// 기본 성공 응답 (200 OK)
	public static <T> CommonResponse<T> success(String message) {
		return new CommonResponse<>("SUCCESS", message, null, HttpStatus.OK);
	}

	// 데이터와 함께 성공 응답 (200 OK)
	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<>("SUCCESS", "요청이 성공적으로 처리되었습니다.", data, HttpStatus.OK);
	}

	// 메시지와 데이터를 포함한 성공 응답 (200 OK)
	public static <T> CommonResponse<T> success(String message, T data) {
		return new CommonResponse<>("SUCCESS", message, data, HttpStatus.OK);
	}

	// 생성 성공 응답 (201 CREATED)
	public static <T> CommonResponse<T> created(T data) {
		return new CommonResponse<>("CREATED", "리소스가 성공적으로 생성되었습니다.", data, HttpStatus.CREATED);
	}

	// 처리 승인 응답 (202 ACCEPTED)
	public static <T> CommonResponse<T> accepted(String message) {
		return new CommonResponse<>("ACCEPTED", message, null, HttpStatus.ACCEPTED);
	}

}
