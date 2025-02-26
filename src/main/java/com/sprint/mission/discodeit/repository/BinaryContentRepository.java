package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {

    // 저장
    BinaryContent save(BinaryContent binaryContent);

    // 모든 BinaryContent 찾기
    Map<UUID, BinaryContent> findAll();

    // BinaryContent의 id로 찾기
    BinaryContent findById(UUID id);

    // BinaryContent의 uuid로 삭제
    void delete(UUID id);


    // 테스트용
    void clearData();
    void resetData();
}
