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
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private String UUID;
  private String username;
  private String password;
  private String email;

  private Instant createdAt;
  private Instant updatedAt;
  private String profileId;
  private UserStatus status;

  public static class UserBuilder{
    private String UUID = UuidGenerator.generateUUID();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private String profileId = "";
    private UserStatus status = null;
    public String getUUID(){
      return this.UUID;
    }
  }

  @Override
  public String toString() {
    return "User{"
        + "UUID='" + UUID + '\''
        + ", username='" + username + '\''
        + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt
        + ", profileImage=" + profileId
        + ", status=" + status
        + '}';
  }

  public void updateProfile(
      String username,
      String email,
      String password
  ) {
    if (username != null) this.username = username;
    if (email != null && email.contains("@")) this.email = email;
    if (password != null) this.password = password;
    this.updatedAt = Instant.now();
  }

  public void updateStatus(UserStatus status){
    this.status = status;
  }

  public void updateProfileImage(String profileId){
    this.profileId = profileId;
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
