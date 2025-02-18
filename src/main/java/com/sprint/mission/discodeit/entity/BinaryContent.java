package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    // 디폴트 생성자로 객체 생성 시, 자동으로 랜덤 UUID가 부여됩니다.
    private UUID id = UUID.randomUUID();
    private byte[] data;
}
