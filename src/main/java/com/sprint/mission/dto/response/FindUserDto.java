package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.User;
import java.util.UUID;

import java.time.Instant;


public record FindUserDto(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    String name,
    String email,
    UUID profileImgId,
    boolean isOnline) {

  public static FindUserDto fromEntityAndStatus(User user, Boolean isOnline) {
    return new FindUserDto(
        user.getId(),
        user.getCreateAt(),
        user.getUpdateAt(),
        user.getName(),
        user.getEmail(),
        user.getProfileImgId(),
        isOnline
    );
  }
}
