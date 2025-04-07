package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

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
