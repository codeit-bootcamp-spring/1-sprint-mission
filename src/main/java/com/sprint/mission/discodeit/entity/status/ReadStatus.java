package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ReadStatus extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

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

  public void setLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}

