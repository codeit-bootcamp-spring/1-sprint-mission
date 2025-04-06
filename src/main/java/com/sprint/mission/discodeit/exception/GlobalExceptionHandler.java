package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sprint.mission.discodeit.exception.user.UserException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = getErrorMessage(e);

		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					Instant.now(),
					status.getReasonPhrase(),
					message,
					null,
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.BAD_REQUEST;

		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					Instant.now(),
					status.getReasonPhrase(),
					"올바른 JSON 형식이 아닙니다. Key 혹은 Value 를 확인해주세요",
					null,
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}

	private String getErrorMessage(MethodArgumentNotValidException e) {
		String message = null;
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		for (ObjectError error : allErrors) {
			message = Arrays.stream(Objects.requireNonNull(error.getCodes()))
				.map(c -> {
					Object[] arguments = error.getArguments();
					Locale locale = LocaleContextHolder.getLocale();
					try {

						return messageSource.getMessage(c, arguments, locale);
					} catch (NoSuchMessageException ee) {
						return null;
					}
				}).filter(Objects::nonNull)
				.findFirst()
				.orElse(error.getDefaultMessage());
		}
		return message;
	}

	@ExceptionHandler(DiscodeitException.class)
	public ResponseEntity<ErrorResponse> handleException(UserException e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					e.getTimestamp(),
					e.getErrorCode().getMessage(),
					e.getMessage(),
					e.getDetails(),
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					Instant.now(),
					status.getReasonPhrase(),
					e.getMessage(),
					null,
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleException(NoSuchElementException e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					Instant.now(),
					status.getReasonPhrase(),
					e.getMessage(),
					null,
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		e.printStackTrace();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseEntity
			.status(status)
			.body(
				new ErrorResponse(
					Instant.now(),
					status.getReasonPhrase(),
					e.getMessage(),
					null,
					e.getClass().getSimpleName(),
					status.value()
				)
			);
	}
}
