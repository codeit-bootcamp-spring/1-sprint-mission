package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
@Service
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  private final BinaryContentMapper binaryContentMapper;

  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateDTO binaryContentCreateDTO) {

    byte[] bytes = binaryContentCreateDTO.bytes();

    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(binaryContentCreateDTO.fileName())
        .size(binaryContentCreateDTO.size())
        .contentType(binaryContentCreateDTO.contentType())
        .build();

    try {
      //파일 메타 정보를 DB에 저장
      binaryContentRepository.save(binaryContent);
      //bytes를 로컬에 저장
      binaryContentStorage.put(binaryContent.getId(), bytes);
      log.info("File created successfully with ID: {} and file name: {}", binaryContent.getId(),
          binaryContent.getFileName());
      return binaryContentMapper.toDto(binaryContent);
    } catch (Exception e) {
      log.error("Error: File with ID: {} creating failed: {}", binaryContent.getId(),
          e.getMessage());
      throw e;
    }
  }

  @Override
  public BinaryContentDto findById(UUID uuid) {
    BinaryContent binaryContent = binaryContentRepository.findById(uuid).orElseThrow(()
        -> new NoSuchElementException("BinaryContent Not Found"));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> uuidList) {
    List<BinaryContent> binaryContentList = binaryContentRepository.findAllByIdIn(uuidList);
    return binaryContentList.stream().map(binaryContentMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public void delete(UUID uuid) {
    binaryContentRepository.deleteById(uuid);
  }
}
