package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent {
    //이미지, 파일 등 바이너리 데이터 표현 도메인 -> 사용자의 프로필 이미지 or 메시지 첨부 파일 표현
    private final UUID id;
    private final Instant createdAt;

    private final UUID contentId; //메시지 혹은 유저 아이디 -> User나 Message가 생성하면, 이 아이디를 참조할 필요가 없다.
    private final String filePath; //file 경로 문자열로 생각했는데, 다른 좋은 타입이 있을까?

    public BinaryContent(UUID contentId, String filePath){
        this.id=UUID.randomUUID();
        this.contentId=contentId;
        this.createdAt=Instant.now();
        this.filePath=filePath;
    }


}
