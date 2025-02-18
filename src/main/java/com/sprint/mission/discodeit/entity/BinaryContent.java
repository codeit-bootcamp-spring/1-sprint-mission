package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();

    private UUID userId;  // BinaryContent와 연관된 User
    private String fileType;  // 파일 타입 (예: image/jpeg, application/pdf 등)
    private byte[] content;  // 바이너리 데이터

    // 수정 불가능하므로 updateAt 필드는 없음
}
