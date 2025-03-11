package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentResponse create(CreateBinaryContentRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(fileName, contentType);
    try {
      UUID fileUUID = binaryContentStorage.put(binaryContent.getId(), bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return BinaryContentMapper.INSTANCE.toBinaryContentResponse(
        binaryContentRepository.save(binaryContent));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<BinaryContentResponse> getBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Binarycontent with " + id + " not found")
    );
    return Optional.ofNullable(
        BinaryContentMapper.INSTANCE.toBinaryContentResponse(binaryContent));
  }

  @Override
  @Transactional
  public BinaryContentResponse saveBinaryContent(BinaryContent binaryContent) {
    return BinaryContentMapper.INSTANCE.toBinaryContentResponse(
        binaryContentRepository.save(binaryContent));
  }

  @Override
  @Transactional
  public void deleteBinaryContent(UUID id) {
    binaryContentRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentResponse> getBinaryContentListByIds(List<UUID> ids) {
    List<BinaryContent> contents = binaryContentRepository.findAllById(ids);
    return BinaryContentMapper.INSTANCE.toResponseList(contents);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<?> downloadBinaryContent(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Binary content not found"));
    return binaryContentStorage.download(BinaryContentResponse.fromEntity(binaryContent));
  }
}
