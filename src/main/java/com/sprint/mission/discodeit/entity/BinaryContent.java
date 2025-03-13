package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent implements Serializable {
    private static final Long serialVersionUID = 1L;

    //이미지, 파일 등 바이너리 데이터 표현 도메인 -> 사용자의 프로필 이미지 or 메시지 첨부 파일 표현
    private final UUID id;
    private final Instant createdAt;

    private final String filePath; //file 경로 문자열

    //TODO: Sprint 3 image or file 구분하는 필드  -> 멀티패스파일?


    //TODO Spring3 ENtitiy가 DTo에 의존하면 안된다. 파라미터로 수정, build로 만들기
    public BinaryContent(BinaryContentCreateDTO binaryContentCreateDTO) {
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.filePath=binaryContentCreateDTO.filePath();
    }





}
