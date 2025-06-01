package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public class BinaryContentUtil {

  // MultipartFile -> Optional<CreateBinaryContentRequest>
  public static Optional<BinaryContentCreateRequest> convertToBinaryContentRequest(
      MultipartFile image) {

    return Optional.ofNullable(image)
        .flatMap(BinaryContentUtil::resolveImageRequest);
  }

  // List<MultipartFile> -> List<CreateBinaryContentRequest>
  public static List<BinaryContentCreateRequest> convertToBinaryContentRequests(
      List<MultipartFile> images) {

    return Optional.ofNullable(images)
        .map(files -> files.stream()
            .map(BinaryContentUtil::resolveImageRequest)
            .flatMap(Optional::stream)
            .toList())
        .orElse(new ArrayList<>());
  }


  private static Optional<BinaryContentCreateRequest> resolveImageRequest(
      MultipartFile multipartFile) {

    if (multipartFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentRequest = new BinaryContentCreateRequest(
            multipartFile.getOriginalFilename(),
            multipartFile.getContentType(),
            multipartFile.getBytes()
        );
        return Optional.of(binaryContentRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
