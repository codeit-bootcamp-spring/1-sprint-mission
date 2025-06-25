package com.sprint.mission.discodeit.entity;

public enum BinaryContentUploadStatus {
    WAITING,   // 업로드 대기 중 (파일 저장 요청만 완료된 상태)
    SUCCESS,   // 업로드 완료
    FAILED     // 업로드 실패 (모든 재시도 실패)
} 