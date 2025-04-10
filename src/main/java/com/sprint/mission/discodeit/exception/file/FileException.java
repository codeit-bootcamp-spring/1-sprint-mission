package com.sprint.mission.discodeit.exception.file;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileException extends DiscodeitException {
	public FileException(ErrorCode code) {
		super(code);
	}

	public FileException(ErrorCode code, Map<String, Object> details) {
		super(code, details);
	}
}
