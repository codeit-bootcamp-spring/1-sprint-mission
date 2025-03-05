package com.sprint.mission.discodeit.global.util;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileConverter {
    public byte[] toByteArray(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILE_CONVERSION_FAIL.format(e.getMessage()));
        }
    }

    public MultipartFile toMultipartFile(byte[] byteArray) {
        return new CustomMultipartFile(byteArray);
    }
}
