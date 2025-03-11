package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContentResponse create(MultipartFile file) {
    BinaryContent newFile = BinaryContent.createBinaryContent(
        file.getName(), file.getSize(), file.getContentType(), convertToBytes(file));
    log.info("Create User Profile : {}", newFile);
    BinaryContent newBinaryContent = binaryContentRepository.save(newFile);
    return BinaryContentResponse.entityToDto(newBinaryContent);
  }

  @Override
  public BinaryContentResponse findByIdOrThrow(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.BINARY_CONTENT_NOT_FOUND, "id :" + id));
    return BinaryContentResponse.entityToDto(binaryContent);
  }

  @Override
  public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids).stream()
        .map(BinaryContentResponse::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    binaryContentRepository.deleteById(id);
  }

  private byte[] convertToBytes(MultipartFile imageFile) {
    try {
      return imageFile.getBytes();
    } catch (IOException e) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR, "");
    }
  }
}
