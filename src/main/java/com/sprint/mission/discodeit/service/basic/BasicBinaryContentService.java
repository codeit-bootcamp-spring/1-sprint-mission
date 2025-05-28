package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.global.exception.binarycontent.FileConversionException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
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
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContentResponse create(MultipartFile file) {
    BinaryContent newFile = BinaryContent.createBinaryContent(
        file.getName(), file.getSize(), file.getContentType());

    BinaryContent newBinaryContent = binaryContentRepository.save(newFile);

    binaryContentStorage.put(newBinaryContent.getId(), convertToBytes(file));

    log.info("Save User Profile success - profileId: {}", newBinaryContent.getId());
    return binaryContentMapper.entityToDto(newBinaryContent);
  }

  @Override
  public BinaryContentResponse findById(UUID id) {
    BinaryContent binaryContent = findByIdOrThrow(id);
    return binaryContentMapper.entityToDto(binaryContent);
  }

  @Override
  public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids).stream()
        .map(binaryContentMapper::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    findByIdOrThrow(id);
    binaryContentRepository.deleteById(id);
  }

  private byte[] convertToBytes(MultipartFile imageFile) {
    try {
      return imageFile.getBytes();
    } catch (IOException e) {
      throw new FileConversionException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private BinaryContent findByIdOrThrow(UUID id) {
    return binaryContentRepository.findById(id).orElseThrow(() ->
        new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, Map.of("id", id)));
  }
}
