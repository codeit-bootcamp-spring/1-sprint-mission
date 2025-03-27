package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  private final BinaryContentMapper binaryContentMapper;

  @Override
  public BinaryContentDto create(BinaryContentCreateDTO binaryContentCreateDTO) {
    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(binaryContentCreateDTO.fileName())
        .size(binaryContentCreateDTO.size())
        .contentType(binaryContentCreateDTO.contentType())
        .bytes(binaryContentCreateDTO.bytes())
        .build();

    return binaryContentMapper.toDto(binaryContentRepository.save(binaryContent));
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
