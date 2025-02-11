package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUpdateDTO {
    private UUID id;
    private String name;
    private String description;
    private boolean isPrivate; // ✅ 추가: Private 채널 여부 확인

    public void validateUpdate() {
        if (isPrivate) {
            throw new IllegalArgumentException("Private channels cannot be updated.");
        }
        if (name == null || description == null) {
            throw new IllegalArgumentException("Name and description cannot be null.");
        }
    }
}
