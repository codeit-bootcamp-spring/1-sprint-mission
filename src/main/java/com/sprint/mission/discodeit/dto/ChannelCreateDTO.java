package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor  // ✅ 기본 생성자 추가
@AllArgsConstructor
public class ChannelCreateDTO {
    private String name;
    private String description;
    private UUID creatorId;
}
