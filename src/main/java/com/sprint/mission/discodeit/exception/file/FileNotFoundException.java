package com.sprint.mission.discodeit.exception.file;

import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileNotFoundException extends FileException {
	public FileNotFoundException(UUID fileId) {
		super(ErrorCode.FILE_NOT_FOUND, Map.of("fileId", fileId));
	}
}
