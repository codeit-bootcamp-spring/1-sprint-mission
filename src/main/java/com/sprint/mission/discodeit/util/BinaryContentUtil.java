package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

public class BinaryContentUtil {
  private BinaryContentUtil() {
  }

  public static byte[] convertToBytes(MultipartFile mFile) {
    try {
      return mFile.getBytes();
    } catch (IOException e) {
      throw new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE);
    }
  }

  public static String convertToBase64(BinaryContent binaryContent) {
    if (binaryContent == null || binaryContent.getData() == null) {
      return null;
    }
    return Base64.getEncoder().encodeToString(binaryContent.getData());
  }

  public static List<String> convertMultipleBinaryContentToBase64(List<BinaryContent> contents) {
    if (contents == null || contents.isEmpty()) {
      return Collections.emptyList();
    }

    return contents.stream()
        .map(BinaryContentUtil::convertToBase64)
        .toList();
  }
}
