package com.sprint.mission.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Getter
public class BinaryContent {

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)

    private final UUID id;
    private final Instant createdAt;

    private final byte[] bytes;
    private BinaryContentIdType binaryContentIdType;

    private BinaryContent(byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.bytes = bytes;
    }

    public static BinaryContent createUserProfile(UUID userId, byte[] bytes){
        BinaryContent bc = new BinaryContent(bytes);
        bc.userId = userId;
        return bc;
    }

    public static BinaryContent createMessageContent(UUID messageId, byte[] bytes){
        BinaryContent bc = new BinaryContent(bytes);
        bc.messageId = messageId;
        return bc;
    }

}
