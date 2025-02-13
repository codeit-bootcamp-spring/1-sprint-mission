package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record BinaryContentCreateRequest (
        UUID userId,
        UUID messageId
){
    public void validate(UUID userId,
                         UUID messageId){
        if(userId == null && messageId == null){
            throw new IllegalArgumentException("userId, messageId 둘 중 하나는 추가되어야 합니다.");
        }
    }
}
