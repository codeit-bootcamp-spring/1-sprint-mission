package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiptUpdateDTO {
    private Instant receivedAt; // ✅ `readAt` → `receivedAt` 네이밍 통일
}
