package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {

    // 저장
    BinaryContent save(BinaryContent binaryContent);

    // 메세지 아이디로 BinaryContent 찾기
    BinaryContent findByMessageId(UUID messageId);

    // 유저 아이디로 BinaryContent 찾기
    Map<UUID, BinaryContent> findByUserId(UUID userId);

    // 모든 BinaryContent 찾기
    Map<UUID, BinaryContent> findAll();

    // BinaryContent의 uuid로 찾기
    BinaryContent findById(UUID id);

    // BinaryContent의 uuid로 삭제
    void remove(UUID id);

    // 유저의 파일 모두 지우기
    void removeAllContentOfUser(UUID userId);

    // 메세지 아이디로 전부 지우기
    Map<UUID, BinaryContent> removeContent(UUID messageId);

}
