package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContentStatusChangedEvent {

  private final BinaryContentDto binaryContent;
  private final UUID userId; // 파일을 업로드한 사용자
}

