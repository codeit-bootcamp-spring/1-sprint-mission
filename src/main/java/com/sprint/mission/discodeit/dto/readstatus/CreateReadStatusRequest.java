package com.sprint.mission.discodeit.dto.readstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateReadStatusRequest {
    private UUID userid;
    private UUID channelid;
}
