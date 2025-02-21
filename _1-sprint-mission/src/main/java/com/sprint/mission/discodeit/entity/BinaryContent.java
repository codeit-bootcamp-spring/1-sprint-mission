package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
//바이너리 데이터(이미지,파일)를 표현하는 도메인 모델
//사용자의 프로필 이미지,메시지에 첨부된 파일을 저장하기 위해 활용
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String fileName; // 파일명
    private String contentType; // 타입

    private byte[] data; // 바이너리 데이터

    private Instant createdAt; //생성 시점

    public BinaryContent(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.createdAt = Instant.now();
    }
}

