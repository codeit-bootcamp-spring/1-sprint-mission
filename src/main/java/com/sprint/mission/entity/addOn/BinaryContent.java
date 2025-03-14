package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseEntity;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;
@Entity
@EqualsAndHashCode(of = {"fileName", "contentType", "size"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString @Getter
@Schema(description = "바이너리 컨텐츠")
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    private String fileName;
    private String contentType;
    private Long size;
    // 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
    // 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용
    // 수정 불가능한 도메인 모델
    // updateAt 필드는 정의 X
    // id 참조 필드 추가 (user, message)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;
}
