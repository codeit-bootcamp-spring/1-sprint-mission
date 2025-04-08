package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "read_statuses")
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private Instant lastReadAt;

  public ReadStatus(Channel channel, User user, Instant lastReadAt) {
    this.channel = channel;
    this.user = user;
    this.lastReadAt = lastReadAt;
  }
}

