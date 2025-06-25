package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SseEvent {

  public final static String NOTI_EVENT = "notifications";
  public final static String BIN_STATUS_EVENT = "binaryContents.status";
  public final static String CHANNEL_REFRESH = "channels.refresh";
  public final static String USER_REFRESH = "users.refresh";

  private String id;
  private Object object;


}
