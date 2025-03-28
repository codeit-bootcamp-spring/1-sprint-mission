package com.sprint.mission.discodeit.exception;

public class FileUploadException extends FileException {
	public FileUploadException() {
		super(ErrorCode.FILE_UPLOAD_ERROR);
	}

	public FileUploadException(String message) {
		super(ErrorCode.FILE_UPLOAD_ERROR, message);
	}
}
