package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  public BinaryContent create(MultipartFile file) {

    long size = file.getSize();
    String fileName = file.getOriginalFilename();
    String contentType = fileName.substring(fileName.lastIndexOf('.'));

    BinaryContent content = binaryContentRepository
        .save(BinaryContent.create(size, fileName, contentType));
    byte[] data;
    try {
      data = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    binaryContentStorage.put(content, data);

    return content;
  }

  @Transactional
  public List<BinaryContent> create(List<MultipartFile> files) {
    if (files == null) {
      return Collections.emptyList();
    }
    return files.stream()
        .map(this::create)
        .toList();
  }

  public BinaryContentDto find(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException(Map.of("id", id)));
    return binaryContentMapper.toDto(content);
  }

  @Transactional
  public void delete(UUID id) {
    binaryContentStorage.delete(id);
    binaryContentRepository.deleteById(id);
  }
}
