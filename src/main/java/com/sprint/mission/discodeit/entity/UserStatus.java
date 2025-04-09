package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user-statuses")
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @JoinColumn(name = "user_id", nullable = false, unique = true)
  @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  // TODO : 롬복 말고 생성자를 명시해줬다면 이런 식이었을 것. 안에서 id, createdAT 값 넣어주는데 파라미터로는 전달 X -> 기본 고정값
//    public UserStatus(UUID userId, Instant lastActiveAt) {
//        this.id = UUID.randomUUID();
//        this.createdAt = Instant.now();
//        //
//        this.userId = userId;
//        this.lastActiveAt = lastActiveAt;
//    }

  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }

//    고치기 전
//    public void isOnline(){
//        if(lastActiveAt!=null && Instant.now().minusSeconds(300).isBefore(lastActiveAt)) {
//            isActive = true;
//        } else {
//            isActive = false;
//        }
//    }


  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

}
