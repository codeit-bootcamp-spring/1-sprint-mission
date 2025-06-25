package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileUploadStatusChangedEvent {

    private final UUID userId;
    private final BinaryContentResponse binaryContentResponse;
}
