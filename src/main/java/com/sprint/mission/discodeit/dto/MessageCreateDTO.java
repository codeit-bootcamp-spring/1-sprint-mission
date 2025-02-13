package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDTO {
    private UUID senderId;       // 메시지를 보낸 사용자 ID
    private UUID channelId;      // 메시지가 속한 채널 ID
    private String content;      // 메시지 내용
    private List<UUID> attachmentIds; // 첨부파일 ID 목록 (BinaryContent)
}
