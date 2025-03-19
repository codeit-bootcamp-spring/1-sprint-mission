package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

/*  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateRequest dto) {

    BinaryContent binaryContent =
        new BinaryContent(dto.getBytes(),
            dto.getFileName(),
            dto.getContentType(),
            (long) dto.getBytes().length);
    binaryContentRepository.save(binaryContent);
    return binaryContentMapper.toDto(binaryContent);
  }*/

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto find(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
    return binaryContentMapper.toDto(binaryContent);
  }

/*
  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAll() {
    return binaryContentRepository.findAll().stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }
*/

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllByIdIn(ids).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

/*  @Override
  @Transactional
  public void delete(UUID id) {
    binaryContentRepository.deleteById(id);
  }*/
}
