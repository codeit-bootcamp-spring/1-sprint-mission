package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor // TODO : 롬복은 나중에 생성자 호출할 때 파라미터로 뭘 줘야하는지 헷갈림
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = null;

  @NonNull
  private String username; // TODO : int같은 기본 타입은 null이 될 수가 없더라도 @NonNull로 명시를 해줘야 @RequiredArgsConstructor가 인식을 하는거 아닌가..?
  @NonNull
  private String email;
  @NonNull
  private String password;
  @NonNull
  private UUID profileId;
  // TODO : User에 직접적으로 channelId 연결 안하는 이유 > 채널 서비스에서 participantIds 가져오는 toDto 관련 > 유저레포지토리에 findByChannelId해서 할 수도 있는데,,?


  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
    boolean anyValueUpdated = false;
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
      anyValueUpdated = true;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      anyValueUpdated = true;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}


