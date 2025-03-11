package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateDTO binaryContentCreateDTO) {
    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(binaryContentCreateDTO.fileName())
        .size(binaryContentCreateDTO.size())
        .contentType(binaryContentCreateDTO.contentType())
        .bytes(binaryContentCreateDTO.bytes())
        .build();

    binaryContentRepository.save(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent findById(UUID uuid) {
    BinaryContent binaryContent = binaryContentRepository.findById(uuid).orElseThrow(()
        -> new NoSuchElementException("BinaryContent Not Found"));
    return binaryContent;
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> uuidList) {
    List<BinaryContent> binaryContentList = binaryContentRepository.findAllByIdIn(uuidList);
    return binaryContentList;
  }

  @Override
  public void delete(UUID uuid) {
    binaryContentRepository.deleteById(uuid);
  }
}
