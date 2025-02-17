package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    
    // 프로필 이미지를 등록할 때, 메세지에 첨부 파일을 등록할 때 두 가지 로직이 구현돼 있어야 한다.
    BinaryContent createBinaryContent(BinaryContentCreateRequest binaryContentCreateRequest);

    BinaryContent findBinaryContentById(UUID binaryContentId);
    // [ ] id 목록으로 조회합니다. List로 조회를 하자는 거겠지?...
    List<BinaryContent> findAllById();

    void deleteBinaryContentByUserId(UUID userId);
    void deleteBinaryContentByMessageId(UUID messageId);
}
