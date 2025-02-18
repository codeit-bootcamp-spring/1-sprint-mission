package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private String UUID;
  private String username;
  private String password;
  private String email;
  private String nickname;
  private String phoneNumber;
  private String description;

  private Instant createdAt;
  private Instant updatedAt;
  private BinaryContent profileImage;
  private UserStatus status;

  public static class UserBuilder{
    private String UUID = UuidGenerator.generateUUID();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private BinaryContent profileImage = null;
    private UserStatus status = null;
  }

  @Override
  public String toString() {
    return "User{"
        + "UUID='" + UUID + '\''
        + ", username='" + username + '\''
        + ", email='" + email + '\''
        + ", nickname='" + nickname + '\''
        + ", phoneNumber='" + phoneNumber
        + '\'' + ", description='" + description + '\''
        + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt
        + ", profileImage=" + (profileImage != null ? "Exists" : "None")
        + ", status=" + status
        + '}';
  }

  public void updateProfile(
      String username,
      String email,
      String nickname,
      String phoneNumber,
      String description,
      String password
  ) {
    if (username != null) this.username = username;
    if (email != null && email.contains("@")) this.email = email;
    if (nickname != null) this.nickname = nickname;
    if (phoneNumber != null) this.phoneNumber = phoneNumber;
    if (description != null) this.description = description;
    if (password != null) this.password = password;
    this.updatedAt = Instant.now();
  }

  public void updateStatus(UserStatus status){
    this.status = status;
  }

  public void updateProfileImage(BinaryContent profileImage){
    this.profileImage = profileImage;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(UUID, user.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
