package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContent create(CreateBinaryContentRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(fileName, contentType);
    try {
      UUID fileUUID = binaryContentStorage.put(binaryContent.getId(), bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public Optional<BinaryContent> getBinaryContent(UUID id) {
    return binaryContentRepository.findById(id);
  }

  @Override
  public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public void deleteBinaryContent(UUID id) {
    binaryContentRepository.deleteById(id);
  }

  @Override
  public List<BinaryContent> getBinaryContentListByIds(List<UUID> ids) {
    return (List<BinaryContent>) binaryContentRepository.findAllById(ids);
  }

  @Override
  public ResponseEntity<?> downloadBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Binary content not found"));
    return binaryContentStorage.download(BinaryContentResponse.fromEntity(binaryContent));
  }
}
