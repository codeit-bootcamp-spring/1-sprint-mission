package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "Name cannot be blank") // 채널 이름이 비어 있지 않아야 합니다.
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters") // 채널 이름 길이 제한
    String name,
    @NotBlank(message = "Description cannot be blank") // 설명이 비어 있지 않아야 합니다.
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") // 설명 길이 제한
    String description
) {

}
