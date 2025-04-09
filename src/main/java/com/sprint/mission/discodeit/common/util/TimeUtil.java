package com.sprint.mission.discodeit.common.util;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

  public static Instant getCurrentTime() {
    return Instant.now();
  }
}
