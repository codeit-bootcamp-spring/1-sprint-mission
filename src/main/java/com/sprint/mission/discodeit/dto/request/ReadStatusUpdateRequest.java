package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ReadStatusUpdateRequest {
    Instant newLastReadAt; // TODO찐: date-time으로 수정
}