package com.sprint.mission.discodeit.dto;

import lombok.Builder;

import java.util.UUID;

// 전반적으로 공통된 DTO를 분리
public class CommonDTO {
    @Builder
    public record idResponse(Long id, UUID uuid) {
        public static idResponse from(Long id, UUID uuid) {
            return idResponse.builder().id(id).uuid(uuid).build();
        }
    }
}
