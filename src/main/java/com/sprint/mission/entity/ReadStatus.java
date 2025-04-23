package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;

import static jakarta.persistence.FetchType.*;

@Entity
@EqualsAndHashCode(of = {"user", "channel"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"lastReadAt"})
@Getter
@Schema(description = "메시지 읽음 상태 정보")
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "user_id", columnDefinition = "uuid", unique = true)
  private User user;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid", unique = true)
  private Channel channel;

  @Column(nullable = false, columnDefinition = "timestamp with time zone")
  private Instant lastReadAt;

  public void update(Instant newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }
}
