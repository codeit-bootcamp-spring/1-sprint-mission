package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString @Getter
@Schema(description = "바이너리 컨텐츠")
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    private String fileName;
    private String contentType;
    private Long size;
    private byte[] bytes;

    public BinaryContent(String fileName, String contentType, Long size, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }

    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)
}
