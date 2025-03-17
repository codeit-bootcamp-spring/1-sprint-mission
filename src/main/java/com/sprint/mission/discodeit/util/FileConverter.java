package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileConverter {

  public static BinaryContentCreateRequest resolveProfileRequest(
      MultipartFile profileFile) {

    try {

      return new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }


  public static List<BinaryContentCreateRequest> getAttachmentRequests(
      List<MultipartFile> attachments) {
    return Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
  }
}


