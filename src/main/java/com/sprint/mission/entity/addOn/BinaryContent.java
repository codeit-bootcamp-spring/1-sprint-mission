package com.sprint.mission.entity.addOn;

import com.sprint.mission.dto.request.BinaryContentDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent {

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)

    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private String fileName;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
