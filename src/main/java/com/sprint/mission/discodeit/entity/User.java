package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  // TODO builder가 id, createdAt처럼 기본값 넣어준 걸 무시할 수 있으니까 얘네들은 final 처리

  private final UUID id = UUID.randomUUID(); // TODO 기본생성자는 그냥 null로 초기화하기 때문에 기본값 직접 넣어줘야함
  private final Instant createdAt = Instant.now(); // TODO 기본생성자는 그냥 null로 초기화하기 때문에 기본값 직접 넣어줘야함
  private Instant updatedAt;

  @NonNull
  private String username; // TODO : int같은 기본 타입은 null이 될 수가 없더라도 @NonNull로 명시를 해줘야 @RequiredArgsConstructor가 인식을 하는거 아닌가..?
  @NonNull
  private String email;
  @NonNull
  private String password;
  private UUID profileId;


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


