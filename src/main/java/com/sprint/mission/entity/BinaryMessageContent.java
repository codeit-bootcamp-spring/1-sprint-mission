package com.sprint.mission.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Getter
public class BinaryMessageContent {

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)
    private final UUID id;
    private final UUID messageId;
    private final Instant createdAt;
    private final byte[] bytes;

    public BinaryMessageContent(UUID messageId, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.messageId = messageId;
        this.createdAt = Instant.now();
        this.bytes = bytes;
    }
}
