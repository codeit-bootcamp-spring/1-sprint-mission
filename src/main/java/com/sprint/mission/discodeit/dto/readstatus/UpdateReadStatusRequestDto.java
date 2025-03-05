package com.sprint.mission.discodeit.dto.readstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateReadStatusRequestDto {
    private  Instant newLastReadAt;
}
