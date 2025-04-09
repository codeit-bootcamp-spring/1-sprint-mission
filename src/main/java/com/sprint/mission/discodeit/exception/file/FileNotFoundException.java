package com.sprint.mission.discodeit.exception.file;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileNotFoundException extends FileException {
	public FileNotFoundException() {
		super(ErrorCode.FILE_DOWNLOAD_ERROR);
	}

	public FileNotFoundException(Map<String, Object> details) {
		super(ErrorCode.FILE_DOWNLOAD_ERROR, details);
	}
}
