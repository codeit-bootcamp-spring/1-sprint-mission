package com.spirnt.mission.discodeit.dto.user;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.enity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
// 유저 정보를 읽기 위한 DTO
public class UserDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String username;
  private String email;
  private Boolean online;
  private BinaryContentDto profile;

  @Builder
  public UserDto(UUID id, Instant createdAt, Instant updatedAt, String username, String email,
      Boolean online, BinaryContentDto profile) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.username = username;
    this.email = email;
    this.online = online;
    this.profile = profile;
  }

  public static UserDto from(User user) {
    return UserDto.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .username(user.getUsername())
        .email(user.getEmail())
        .online(user.getStatus().isOnline())
        .profile((user.getProfile() != null) ? BinaryContentDto.from(user.getProfile()) : null)
        .build();
  }
}
