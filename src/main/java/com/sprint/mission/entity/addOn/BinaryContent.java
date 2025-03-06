package com.sprint.mission.entity.addOn;

import com.sprint.mission.config.BaseTimeEntity;
import com.sprint.mission.dto.request.BinaryContentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "바이너리 컨텐츠")
public class BinaryContent extends BaseTimeEntity {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private String fileName;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)

}
