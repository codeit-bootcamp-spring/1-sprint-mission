package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

import java.time.Instant;


@Schema(description = "유저 정보")
public record FindUserDto(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    String name,
    String email,
    UUID profileImgId,
    boolean isOnline) {

  public FindUserDto(User user, Boolean isOnline) {
    this(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile().getId(),
        isOnline
    );
  }


//  public static FindUserDto toDtoFromEntityAndStatus(User user, Boolean isOnline) {
//    return new FindUserDto(
//        user.getId(),
//        user.getCreatedAt(),
//        user.getUpdatedAt(),
//        user.getName(),
//        user.getEmail(),
//        user.getProfileImgId(),
//        isOnline
//    );
//  }
}
