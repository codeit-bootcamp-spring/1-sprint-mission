package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContentDto create(MultipartFile file) throws RuntimeException {
    try {
      BinaryContent binaryContent = new BinaryContent(
          file.getName(),
          file.getContentType(),
          file.getSize()
      );
      BinaryContent savedContent = binaryContentRepository.save(binaryContent);
      UUID contentId = binaryContentStorage.put(binaryContent.getId(), file.getBytes());
      return binaryContentMapper.toDto(savedContent);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto findById(String contentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(UUID.fromString(contentId))
        .orElse(null);
    if (binaryContent == null) {
      throw new RuntimeException("Content not found");
    }
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<String> contentIds) {

    List<BinaryContent> list = binaryContentRepository.findAll().stream()
        .filter(b -> contentIds.contains(b.getId())).toList();
    return list.stream().map(binaryContentMapper::toDto).toList();
  }

  @Override
  @Transactional
  public boolean deleteById(String contentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(UUID.fromString(contentId))
        .orElse(null);

    if (binaryContent == null) {
      throw new RuntimeException("Binary Content not found");
    }
    binaryContentRepository.deleteById(UUID.fromString(contentId));
    return true;
  }
}
