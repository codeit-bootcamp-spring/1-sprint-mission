package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadStatusUpdateRequest {

    //    @NotNull
//    private Instant newLastReadAt;
    private Boolean newNotificationEnabled;
}
