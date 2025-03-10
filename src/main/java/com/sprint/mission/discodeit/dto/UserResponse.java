package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String name,
    String email,
    UUID profileId
) {

  public static UserResponse entityToDto(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .name(user.getUsername())
        .email(user.getEmail())
        .profileId(user.getProfile().getId())
        .build();
  }
}
