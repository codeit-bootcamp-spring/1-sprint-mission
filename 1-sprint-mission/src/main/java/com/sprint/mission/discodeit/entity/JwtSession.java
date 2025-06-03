package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtSession {

  @Id
  private UUID userId;

  @Transient
  private UserDto userDto;

  @Column(nullable = false)
  private String accessToken;

  @Column(nullable = false)
  private String refreshToken;
}
