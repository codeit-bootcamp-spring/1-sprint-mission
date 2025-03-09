package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseUpdateEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  private UUID id;

  private String username;
  private String email;
  private String password;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_image_id")
  private BinaryContent profileImage;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ReadStatus> readStatuses = new HashSet<>() {
  };

  @ManyToMany(mappedBy = "users")
  private List<Channel> channels = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "status_id")
  private UserStatus userStatus;

  public User(String username, String email, String password) {
    this.id = UUID.randomUUID();
    this.username = username;
    this.email = email;
    this.password = password;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public void setUserStatus(UserStatus userStatus) {
    if (this.userStatus != null) {
      this.userStatus = userStatus;
      userStatus.setUser(this);
    }
  }
}
