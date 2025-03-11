package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "user_statuses")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final Long serialVersionUID = 1L;

  //사용자의 마지막 접속 시간 표현-> 온라인 상태 확인

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "last_active_at")
  private Instant lastAccessedAt;

  public UserStatus(User user, Instant lastAccessedAt) {
    super();
    this.user = user;
    this.lastAccessedAt = lastAccessedAt;
  }


  //유저 온라인 상태를 마지막 접속 시간이 현재 시간으로부터 5분 이내임을 검증하고 반환하는 메서드.
  public Boolean isOnline() {
    if (lastAccessedAt != null) {
      Duration duration = Duration.between(lastAccessedAt, Instant.now());
      if (duration.toMinutes() <= 5) {
        return true;
      }
    }
    return false;
  }

  public void update(UserStatusUpdateDTO userStatusUpdateDTO) {
    this.lastAccessedAt = userStatusUpdateDTO.time();
    update();
    isOnline();
  }
}
