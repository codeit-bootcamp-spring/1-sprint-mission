package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.response.binary.BinaryContentResponseData;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;

  public BinaryContentResponseData create(BinaryContentCreateDTO createDTO) {
    // 관련된 유저 확인
    User user = userRepository.findById(createDTO.userId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    //새로운 binaryContent 생성, 저장
    BinaryContent binaryContent = new BinaryContent(
        UUID.randomUUID(),
        user.getId(),
        createDTO.messageId(),
        createDTO.filename(),
        "application/octet-stream",
        createDTO.bytes()
    );
    binaryContentRepository.save(binaryContent);

    // DTO 변환 반환
    return BinaryContentResponseData.fromEntity(binaryContent);
  }

  public BinaryContentDTO find(UUID binaryContentId) {
    // ID 기준으로 조회
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
    //DTO 변환 반환
    return BinaryContentDTO.fromEntity(binaryContent);
  }


  public List<BinaryContentResponseData> findAllByIdIn(List<UUID> binaryContentIds) {
    if (binaryContentIds.isEmpty()) {
      return List.of();
    }
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .map(BinaryContentResponseData::fromEntity)
        .toList();
  }

  public List<BinaryContentResponseData> findAllByUserId(UUID userId) {
    return binaryContentRepository.findAllByUserId(userId).stream()
        .map(BinaryContentResponseData::fromEntity)
        .toList();
  }


  public void delete(UUID binaryContentId) {
    // id 기준 으로 조회
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new IllegalArgumentException("BinaryContent not found"));
    //파일 삭제
    binaryContentRepository.delete(binaryContent);

    System.out.println("BinaryContent deleted: " + binaryContentId);
    log.error("BinaryContent deleted: {}", binaryContentId);
  }
}
