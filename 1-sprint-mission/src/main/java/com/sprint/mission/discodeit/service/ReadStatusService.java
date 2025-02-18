package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.DTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request);

    // ✅ ID로 ReadStatus 조회
    ReadStatus findById(UUID id);

    // ✅ 특정 사용자(UserId)와 관련된 모든 ReadStatus 조회
    List<ReadStatus> findAllByUserId(UUID userId);
    //채널 다 돌면서 채널에 있는 readstatus 조회?

    // ✅ ReadStatus 수정
    boolean update(ReadStatusUpdateRequest request);

    // ✅ ReadStatus 삭제
    void delete(List<ReadStatus> list);
    void deleteUser(UUID userId);
    void addReadStatus(ReadStatus readStatus);

}
