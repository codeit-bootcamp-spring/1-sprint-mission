package com.sprint.mission.discodeit.exception;

public class EmployeeAlreadyExistsException extends EmployeeException {

	public EmployeeAlreadyExistsException() {
		super(ErrorCode.EMPLOYEE_ALREADY_EXISTS);
	}
}
