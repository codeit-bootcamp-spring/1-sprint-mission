package com.spirnt.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Schema(description = "Private Channel 생성 정보")
public class PrivateChannelCreateRequest {

  private List<UUID> participantIds;

  public PrivateChannelCreateRequest(List<UUID> participantIds) {
    this.participantIds = (participantIds == null) ? new ArrayList<>() : participantIds;
  }
}
