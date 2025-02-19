package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor  // ✅ 기본 생성자 추가
@AllArgsConstructor  // ✅ 모든 필드를 포함한 생성자 추가
public class BinaryContentDTO {
    private UUID id;
    private String fileName;
    private byte[] data;
    private UUID ownerId;
}
