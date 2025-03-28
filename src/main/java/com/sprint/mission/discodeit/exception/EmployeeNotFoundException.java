package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class EmployeeNotFoundException extends EmployeeException {

	public EmployeeNotFoundException(Map<String, Object> details) {
		super(ErrorCode.EMPLOYEE_NOT_FOUND, details);
	}

	public EmployeeNotFoundException() {
		super(ErrorCode.EMPLOYEE_NOT_FOUND);
	}
}
