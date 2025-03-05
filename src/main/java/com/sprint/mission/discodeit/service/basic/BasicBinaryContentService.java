package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequestDto request) {
    String fileName = request.getFileName();
    byte[] bytes = request.getBytes();
    String contentType = request.getContentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType,
        bytes
    );
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("BinaryContent id not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids).stream()
        .toList();
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.deleteById(id);
  }

  @Override
  public void deleteByMessageId(UUID id) {
    binaryContentRepository.deleteByMessageId(id);
  }

  @Override
  public void deleteByUserId(UUID id) {
    binaryContentRepository.deleteByUserId(id);
  }

  @Override
  public List<BinaryContent> findAll() {
    return binaryContentRepository.findAll();
  }
}
