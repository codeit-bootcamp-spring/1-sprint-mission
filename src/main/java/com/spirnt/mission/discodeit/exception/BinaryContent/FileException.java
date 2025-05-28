package com.spirnt.mission.discodeit.exception.BinaryContent;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * BinaryContent를 스토리지에 저장하거나, 스토리지에서 읽을 때 에러가 발생하는 경우 FileException으로 변환됨
 */
public class FileException extends BinaryContentException {

    public FileException(Map<String, Object> details) {
        super(ErrorCode.FILE_ERROR, details);
    }
}
