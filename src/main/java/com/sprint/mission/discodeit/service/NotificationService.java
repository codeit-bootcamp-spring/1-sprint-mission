package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    // 알림 생성
    // 알림 수정
    // 알리 삭제
    // 알림 조회
    List<NotificationDto> findAll();

    void deleteById(UUID notificationId);

    NotificationDto find(UUID notificationId);
}
