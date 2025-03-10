package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class ReadStatus extends BaseUpdatableEntity {


  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;

  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
  }

  //lastReadAt 시간 수정.
  public void updateReadStatus(Instant time) {
    lastReadAt = time;
  }


}