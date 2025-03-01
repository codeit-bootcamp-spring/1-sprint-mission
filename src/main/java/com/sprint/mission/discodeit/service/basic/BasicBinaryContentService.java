package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentFindResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent createBinaryContent(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(request.fileName(),
        request.size(),
        request.contentType(),
        request.bytes());
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent findBinaryContentById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          throw new NoSuchElementException(
              "binaryContent(" + binaryContentId + ")가 없습니다."); // 전역 404
        });

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    // DTO의 변환은 Service 레이어에서
    return new BinaryContentFindResponse(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
     */

    return binaryContent;
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    return binaryContentRepository.findAll().stream()
        .map(binaryContent ->
            new BinaryContentFindResponse(
                binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType(),
                binaryContent.getBytes()
            )
        ).toList();
     */

    return binaryContentRepository.findAll().stream()
        .filter(binaryContent -> ids.contains(binaryContent.getId()))
        .toList();
  }

  @Override
  public void deleteBinaryContentById(UUID binaryContentId) {
    binaryContentRepository.delete(binaryContentId);
  }


}
