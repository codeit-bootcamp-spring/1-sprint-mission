package com.sprint.mission.discodeit.converter;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Component
public class MultipartFileToBinaryContentConverter {

  public BinaryContentCreateRequest convert(MultipartFile profileFile) {
    try {
      return new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      );
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 처리하는 중 오류 발생", e);
    }

  }

  public List<BinaryContentCreateRequest> convertAttachments(List<MultipartFile> attachments) {
    return (attachments == null || attachments.isEmpty())
        ? List.of()
        : attachments.stream()
            .map(this::convert)
            .toList();
  }
}
