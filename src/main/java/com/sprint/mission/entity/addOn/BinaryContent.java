package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseEntity;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.FetchType.*;

@Entity
@EqualsAndHashCode(of = {"fileName", "contentType", "size"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString(of = {"fileName", "size", "contentType"})
@Getter
@Schema(description = "바이너리 컨텐츠")
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    private String fileName;
    private Long size;
    private String contentType;

    @OneToOne(mappedBy = "profile")
    private User user;

    public BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }

    public void initUserProfile(){

    }

}

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
// 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
// 수정 불가능한 도메인 모델
// updateAt 필드는 정의 X
// id 참조 필드 추가 (user, message)
