package com.sprint.mission.discodeit.exception;

import java.util.Map;

public abstract class EmployeeException extends DiscodeitException {

	protected EmployeeException(ErrorCode errorCode, String message, Map<String, Object> details) {
		super(errorCode, message, details);
	}

	protected EmployeeException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected EmployeeException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}

	protected EmployeeException(ErrorCode errorCode) {
		super(errorCode);
	}
}
