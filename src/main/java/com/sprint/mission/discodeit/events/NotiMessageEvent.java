package com.sprint.mission.discodeit.events;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotiMessageEvent implements Serializable {

  private UUID senderId;
  private UUID channelId;
  private String content;

}
