package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    /**
     * 바이너리 콘텐츠를 생성합니다.
     * @throws RuntimeException 파일 저장 중 오류가 발생한 경우
     */
    BinaryContent create(String fileName, Long size, String contentType, byte[] data);

    /**
     * ID로 바이너리 콘텐츠를 조회합니다.
     * @throws ResourceNotFoundException 해당 ID의 바이너리 콘텐츠가 없는 경우
     */
    BinaryContent find(UUID binaryContentId);

    /**
     * 여러 ID에 해당하는 바이너리 콘텐츠 목록을 조회합니다.
     */
    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

    /**
     * 바이너리 콘텐츠를 삭제합니다.
     * @throws ResourceNotFoundException 해당 ID의 바이너리 콘텐츠가 없는 경우
     */
    void delete(UUID binaryContentId);
}
