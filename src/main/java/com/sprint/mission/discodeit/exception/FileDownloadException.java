package com.sprint.mission.discodeit.exception;

public class FileDownloadException extends FileException {
	public FileDownloadException() {
		super(ErrorCode.FILE_DOWNLOAD_ERROR);
	}

	public FileDownloadException(String message) {
		super(ErrorCode.FILE_DOWNLOAD_ERROR, message);
	}
}
