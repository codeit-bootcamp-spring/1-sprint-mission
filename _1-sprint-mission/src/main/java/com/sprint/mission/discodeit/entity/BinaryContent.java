package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
//바이너리 데이터(이미지,파일)를 표현하는 도메인 모델
//사용자의 프로필 이미지,메시지에 첨부된 파일을 저장하기 위해 활용
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt; //생성 시점

    //메타 정보
    private String fileName; // 파일명
    private Long size; //파일 사이즈
    private String contentType; // 타입
    private byte[] bytes; // 바이너리 데이터

    public BinaryContent(String fileName, long length, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.fileName = fileName;
        this.size = length;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}

