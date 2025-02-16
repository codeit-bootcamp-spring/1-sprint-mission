package com.sprint.mission.discodeit.repository;

import java.util.UUID;

public interface BinaryContentRepository {
    // 프로필 이미지 업로드
    void saveProfileImage(byte[] image, UUID userId);

    // 프로필 이미지 삭제
    void deleteProfileImage(byte[] image);

    // 메세지 파일 저장
    void saveMessageFile(byte[] image, UUID messageId);

    // 모든 도메인 정보 삭제
    void deleteAllByUserId(UUID userId);
}
