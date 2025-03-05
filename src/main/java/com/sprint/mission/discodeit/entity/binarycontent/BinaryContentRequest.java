package com.sprint.mission.discodeit.entity.binarycontent;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * multipart file을 받아올 클래스
 */

@Getter
@AllArgsConstructor
@Builder
public class BinaryContentRequest {

  private UUID channelId;
  private UUID requestUserId;

  @NotBlank
  private String fileName;
  private MultipartFile file;
  private List<MultipartFile> files;

  public BinaryContentRequest(String fileName, MultipartFile file, List<MultipartFile> files) {
    this.fileName = fileName;
    this.file = file;
    this.files = files;
  }

  public UUID updateId(UUID requestUserId) {
    if (this.requestUserId != null) {
      return requestUserId;
    }

    this.channelId = requestUserId;
    this.requestUserId = requestUserId;

    return this.requestUserId;
  }
}
