package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponse(
    UUID id,
    String username,
    String email,
    BinaryContentResponse profile,
    boolean online
) {

//  public static UserResponse entityToDto(User user) {
//    return UserResponse.builder()
//        .id(user.getId())
//        .username(user.getUsername())
//        .email(user.getEmail())
//        .profile(user.getProfile())
//        .online(user.getStatus().getLastActiveAt())
//        .build();
//  }
}
