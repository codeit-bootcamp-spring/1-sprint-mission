package com.sprint.mission.discodeit.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	//IllegalArgumentException 처리
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("IllegalArgumentException: ", e);
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			e.getClass().getSimpleName(),
			ErrorCode.INVALID_INPUT_VALUE.getCode(),
			e.getMessage()
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	//NoSuchElementException 처리
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
		log.error("NoSuchElementException: ", e);
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			e.getClass().getSimpleName(),
			ErrorCode.RESOURCE_NOT_FOUND.getCode(),
			e.getMessage()
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Generic Exception: ", e);
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			e.getClass().getSimpleName(),
			ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
			"An unexpected error occurred"
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// MethodArgumentNotValidException 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		// 로그 출력
		log.error("MethodArgumentNotValidException: ", ex);

		// 유효성 검사 실패한 필드 오류 메시지 처리
		StringBuilder errorMessage = new StringBuilder();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMessage.append(error.getField())
				.append(": ")
				.append(error.getDefaultMessage())
				.append("; ");
		});

		// ErrorResponse 객체 생성
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			ex.getClass().getSimpleName(),
			ErrorCode.INVALID_INPUT_VALUE.getCode(),
			errorMessage.toString() // 오류 메시지 포함
		);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
