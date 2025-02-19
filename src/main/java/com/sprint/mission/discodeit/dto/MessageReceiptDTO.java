package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiptDTO {
    private UUID id;
    private UUID messageId;
    private UUID receiverId;
    private UUID channelId;
    private Instant receivedAt;
}
